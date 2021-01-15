package frc.robot.util;

import javax.lang.model.util.ElementScanner6;

/**
 * Simple PID controller I wrote and modified a bit
 */
public class SpartanPID {

  private double mP = 0.0;
  private double mI = 0.0;
  private double mD = 0.0;

  private double mMinOutput = Double.NEGATIVE_INFINITY;
  private double mMaxOutput = Double.POSITIVE_INFINITY;

  private double mSetpoint = 0.0;

  private double intAccum = 0.0;
  private double intRange = 0.0;
  private double lastErr = Double.NaN;

  public SpartanPID(double p, double i, double d) {
    mP = p;
    mI = i;
    mD = d;
  }

  public double WhatShouldIDo(double current, double deltaT) {

    double error = mSetpoint - current;

    if (!epsilon(error, Math.copySign(error, intAccum)) && !epsilon(intAccum, 0.0)) { // Basically if it over shot the and the integral is causing it, hit the big red button
      //intAccum = 0.0;
    }

    if (Math.abs(intAccum) > intRange / 2) intAccum += error * deltaT; // Add the new stuff

    double derivative = 0.0;
    if (Double.isFinite(lastErr)) {
      derivative = (error - lastErr) / deltaT;
    }
    lastErr = error;

    double p = mP * error;
    double i = mI * intAccum;
    double d = mD * derivative;

    System.out.println(intAccum);

    double sum = p + i + d;
    if (sum > mMaxOutput)
      return mMaxOutput;
    else if (sum < mMinOutput)
      return mMinOutput;
    else
      return sum;

    // return MathUtils.clamp(p + i + d, mMinOutput, mMaxOutput);
  }

  public void reset() { intAccum = 0.0; lastErr = Double.NaN; }

  public boolean epsilon(double a, double b) {
    return Math.abs(a - b) < 1e-9; // Literally just a tolerance because lazy
  }

  // Getters and Setters

  public double getP() { return mP; }
  public double getI() { return mI; }
  public double getD() { return mD; }
  public double getMinOutput() { return mMinOutput; }
  public double getMaxOutput() { return mMaxOutput; }
  public double getSetpoint() { return mSetpoint; }
  public double getIntegralRange() { return intRange; }

  public void setP(double P) { mP = P; }
  public void setI(double I) { mI = I; }
  public void setD(double D) { mD = D; }
  public void setMinOutput(double MinOutput) { mMinOutput = MinOutput; }
  public void setMaxOutput(double MaxOutput) { mMaxOutput = MaxOutput; }
  public void setOutputRange(double Min, double Max) { mMinOutput = Min; mMaxOutput = Max; }
  public void setSetpoint(double Setpoint) { mSetpoint = Setpoint; }
  public void setIntegralRange(double Range) { intRange = Range; }

}