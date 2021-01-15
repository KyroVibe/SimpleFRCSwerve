package frc.robot.util;

import edu.wpi.first.wpilibj.Spark;

/**
 * Basic motor set using a Spark for the drive and azimuth motors
 */
public class ExampleMotorSet extends ModuleMotorSet<Spark, Spark> {

  public ExampleMotorSet(int aziPort, int driPort) {
    super(new Spark(aziPort), new Spark(driPort));

    // Setup motors (Current limits, etc.)
  }

  @Override
  public void SetAzimuthOutput(double val) {
    azimuthMotor.set(val);
  }

  @Override
  public void SetDriveOutput(double val) {
    driveMotor.set(val);
  }
  
}
