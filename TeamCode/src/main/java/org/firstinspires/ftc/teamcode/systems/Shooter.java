package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Ballistics;
import org.firstinspires.ftc.teamcode.common.HiveType;

public class Shooter {
    final double FIRE_SPEED = 4;
    double FIRE_MOTOR_SPEED = 1000;
    private boolean isShooting;

    double goal_height = 54;

    Pose2D[] hive_positions = {
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // red aud
            new Pose2D(DistanceUnit.INCH, 57.1, 85, AngleUnit.DEGREES, 0), // red field
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // blue aud
            new Pose2D(DistanceUnit.INCH, 57.1, 85, AngleUnit.DEGREES, 0)}; // blue field

    DcMotorEx shooterMotorSmall, shooterMotorBig;
    Servo shooterServoSmall, shooterServoBig;
    double R = 10;
    double xoffset = 0;
    double yoffset = 0;

    public Shooter()
    {
    }

    private double GetAngle(Pose2D currentPos, HiveType target, double offsetX, double offsetY)
    {
        Pose2D goalPos = hive_positions[target.ordinal()];
        // distance is x
        double x = Math.sqrt(Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2) +
                Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2));
        return Ballistics.GetFiringAngle(x + offsetX, goal_height + offsetY, FIRE_SPEED);
    }

    public void Shoot()
    {

    }

    public boolean IsShooting()
    {
        return isShooting;
    } 

    public void StartShooting()
    {
        isShooting = true;
        shooterMotorSmall.setVelocity(FIRE_MOTOR_SPEED);
   
        shooterMotorBig.setVelocity(FIRE_MOTOR_SPEED);
    }

    public void StopShooting()
    {
        isShooting = false;
        shooterMotorSmall.setVelocity(0);
        shooterMotorBig.setVelocity(0);
    }
    public void UpdateFireRate(double fireRate)
    {
        FIRE_MOTOR_SPEED += fireRate;
    }
    public void UpdateRatio(double ratio){
        R += ratio;
    }
    public double getXOffset()
    {
        return xoffset;
    }
    public double getYOffset()
    {
        return yoffset;
    }
    public void UpdateXOffset(double xoff){
        xoffset += xoff;
    }
    public void UpdateYOffset(double yoff){
        yoffset += yoff;
    }

    public void UpdateFiring(Pose2D currentPos, HiveType target)
    {
        shooterServoBig.setPosition(GetAngle(currentPos, target, getXOffset(), getYOffset()) * R);
        shooterServoSmall.setPosition(GetAngle(currentPos, target, getXOffset(), getYOffset()) * R);
    }

}
