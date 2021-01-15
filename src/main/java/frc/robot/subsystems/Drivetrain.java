package frc.robot.subsystems;

import frc.robot.util.ExampleMotorSet;

/**
 * 0: Front Right 
 * 1: Front Left 
 * 2: Back Left 
 * 3: Back Right
 */
public class Drivetrain {

  public static final double WHEELBASE = 1.0;
  public static final double TRACKWIDTH = 1.0;

  private Module[] modules;

  public Drivetrain() {
    modules = new Module[4];
    modules[0] = new Module(new ExampleMotorSet(0, 1));
    modules[1] = new Module(new ExampleMotorSet(2, 3));
    modules[2] = new Module(new ExampleMotorSet(4, 5));
    modules[3] = new Module(new ExampleMotorSet(6, 7));
  }

  /**
   * Mixes input and sets the modules desired states
   * @param f Forward
   * @param s Strafe
   * @param r Rotation
   * @param angle Chassis angle
   */
  public void setModules(double f, double s, double r, double angle) {
    double[][] targets = mixer(f, s, r, angle);

    for (int i = 0; i < 4; i++) {
      modules[i].setTargetState(targets[0][i], targets[1][i]);
    }
  }

  /**
   * mixes front, strafe, rotation, and current chassis angle to get speeds and angles of modules
   * @param f Front
   * @param s Strafe
   * @param r Rotation
   * @param angle Current angle of chassis
   * @return [angles, speeds][f right, f left, b left, b right]
   */
  private double[][] mixer(double f, double s, double r, double angle) {

    // Alter f and s to point global front
    double angleRad = Math.toRadians(angle);
    double temp = f * Math.cos(angleRad) + s * Math.sin(angleRad);
    s = -f * Math.sin(angleRad) + s * Math.cos(angleRad);
    f = temp;

    // Calculate angles and speeds
    double a = s - r * (WHEELBASE / TRACKWIDTH);
    double b = s + r * (WHEELBASE / TRACKWIDTH);
    double c = f - r * (TRACKWIDTH / WHEELBASE);
    double d = f + r * (TRACKWIDTH / WHEELBASE);

    double[] angles = new double[] { Math.atan2(b, c) * 180 / Math.PI, Math.atan2(b, d) * 180 / Math.PI,
        Math.atan2(a, d) * 180 / Math.PI, Math.atan2(a, c) * 180 / Math.PI };

    double[] speeds = new double[] { Math.sqrt(b * b + c * c), Math.sqrt(b * b + d * d), Math.sqrt(a * a + d * d),
        Math.sqrt(a * a + c * c) };

    double max = speeds[0];

    // Normalize speeds
    for (double speed : speeds) {
      if (speed > max) {
        max = speed;
      }
    }

    double mod = 1;
    if (max > 1) {
      mod = 1 / max;

      for (int i = 0; i < 4; i++) {
        speeds[i] *= mod;

        angles[i] %= 360;
        if (angles[i] < 0)
          angles[i] += 360;
      }
    }

    // Done
    return new double[][] { angles, speeds };
  }

}
