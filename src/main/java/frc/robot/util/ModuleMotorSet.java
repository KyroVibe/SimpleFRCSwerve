package frc.robot.util;

/**
 * Override this class the create custom motor sets to be used by modules.
 */
public abstract class ModuleMotorSet<Azimuth, Drive> {
  
  public Azimuth azimuthMotor;
  public Drive driveMotor;

  protected ModuleMotorSet(Azimuth azimuthMotor, Drive driveMotor) {
    this.azimuthMotor = azimuthMotor;
    this.driveMotor = driveMotor;
  }

  /**
   * If overridden, sets the output of the azimuth motor
   * @param val Percent output of motor
   */
  public abstract void SetAzimuthOutput(double val);
  /**
   * If overridden, sets the output of the drive motor
   * @param val Percent output of motor
   */
  public abstract void SetDriveOutput(double val);

}
