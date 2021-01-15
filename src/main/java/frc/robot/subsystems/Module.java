package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Robot;
import frc.robot.util.ModuleMotorSet;
import frc.robot.util.SpartanPID;

public class Module {

  // THESE SHOULD VARY MODULE TO MODULE
  public static final double ENCODER_ZERO = 0.0;
  public static final int ENCODER_PORT = 0;
  public static final double P = 1.0, I = 0.0, D = 0.0;

  // THIS SHOULDN'T BUT UNFORTUNATELY MIGHT
  public static final double ENCODER_MAX_VOLTAGE = 5.0; // This should be correct

  private double targetAngle, targetSpeed;

  private AnalogInput encoder;
  private ModuleMotorSet motorSet;

  private SpartanPID aziController;

  /**
   * Creates Module with a ModuleMotorSet to control
   * @param motorSet Motor set with designated motors to control
   */
  public Module(ModuleMotorSet motorSet) {

    this.motorSet = motorSet;

    aziController = new SpartanPID(P, I, D);
    aziController.setOutputRange(-1.0, 1.0);

    encoder = new AnalogInput(ENCODER_PORT);
    encoder.getVoltage(); // 0 -> 5 ish
  }

  /**
   * Set the desired angle and speed of the module
   * @param angle Desired angle of the module
   * @param speed Desired speed of the module
   */
  public void setTargetState(double angle, double speed) {
    double p = limitRange(angle, 0, 360);
    double current = getAngle();

    double delta = current - p;

    if (delta > 180) {
      p += 360;
    } else if (delta < -180) {
      p -= 360;
    }

    delta = current - p;
    if (delta > 90 || delta < -90) {
      if (delta > 90)
        p += 180;
      else if (delta < -90)
        p -= 180;
        speed *= -1;
    } else {
      speed *= 1;
    }

    targetAngle = p;
    targetSpeed = speed;

    aziController.setSetpoint(targetAngle);
    double aziOut = aziController.WhatShouldIDo(getAngle(), Robot.DeltaT);
    
    motorSet.SetAzimuthOutput(aziOut);
    motorSet.SetDriveOutput(targetSpeed);
  }

  /**
   * Give current angle of the module
   * @return Angle in degrees
   */
  public double getAngle() {
    double encoderReading = encoder.getVoltage();
    encoderReading = encoderReading - ENCODER_ZERO;
    encoderReading = limitRange(encoderReading, 0.0, ENCODER_MAX_VOLTAGE);

    double mod = encoderReading / ENCODER_MAX_VOLTAGE;
    return 360.0 * mod;
  }

  public double limitRange(double a, double min, double max) {
    while (a < min) a += (max - min);
    while (a >= max) a -= (max - min);
    return a;
  }

}