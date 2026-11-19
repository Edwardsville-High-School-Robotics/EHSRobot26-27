package org.firstinspires.ftc.teamcode.systems;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Ballistics;
import org.firstinspires.ftc.teamcode.common.Coordinate3D;
import org.firstinspires.ftc.teamcode.common.FireInfo;
import org.firstinspires.ftc.teamcode.common.HiveType;

public class Shooter {
    public static final double FIRE_SPEED = 4;
    public static final double FIRE_ANGLE = 83.56;
    final double WHEEL_CIRCUMFERENCE = 0.096 * Math.PI;
    public static double FIRE_MOTOR_SPEED_OFFSET = 1.13;
    double SHOOT_TIME = 3.0; // run shoot for 3 seconds

    private boolean isShooting;
    private Timer shooterTimer; // times how long we've been shooting

    public static final double goal_height = 54;

    double angle = 0;
    Pose2D[] hive_positions = {
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // red aud
            new Pose2D(DistanceUnit.INCH, 57.1, 85, AngleUnit.DEGREES, 0), // red field
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // blue aud
            new Pose2D(DistanceUnit.INCH, 57.1, 85, AngleUnit.DEGREES, 0)}; // blue field

    DcMotorEx shooterMotorSmall, shooterMotorBig;
    Servo shooterServoSmall, shooterServoBig;
    double R = 10;
    static double zoffset = 0;
    static double yoffset = 0;

    public static final Coordinate3D LL_OFFSET = new Coordinate3D(0,0,0); // offset of the shooter from the camera's position

    public Shooter(HardwareMap map)
    {
        shooterMotorSmall = map.get(DcMotorEx.class, "pollen_shooter");
        shooterMotorSmall.setDirection(DcMotorSimple.Direction.REVERSE);
        isShooting = false;
        shooterTimer = new Timer();
    }

//    private double GetAngle(Pose2D currentPos, HiveType target, double offsetX, double offsetY)
//    {
//        Pose2D goalPos = hive_positions[target.ordinal()];
//        // distance is x
//        double x = Math.sqrt(Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2) +
//                Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2));
//        return Ballistics.GetFiringAngleInch(x + offsetX, goal_height + offsetY, FIRE_SPEED);
//    }

    public void Shoot()
    {
        if (!isShooting)
            StartShooting();
        else if (shooterTimer.seconds() > SHOOT_TIME)
            StopShooting();
    }

    public boolean IsShooting()
    {
        return isShooting;
    }

    public void StartShooting()
    {
        isShooting = true;
        shooterTimer.reset();
    }

    public void StopShooting()
    {
        isShooting = false;
        shooterMotorSmall.setVelocity(0);
//        shooterMotorBig.setVelocity(0);
    }
    public void UpdateFireRateOffset(double fireRate)
    {
        FIRE_MOTOR_SPEED_OFFSET += fireRate;
    }
    public void UpdateRatio(double ratio){
        R += ratio;
    }
    public static double getZOffset()
    {
        return zoffset;
    }
    public static double getYOffset()
    {
        return yoffset;
    }
    public double getRatio()
    {
        return R;
    }
    public double getAngle()
    {
        return angle;
    }
    public double getFireSpeedOffset()
    {
        return FIRE_MOTOR_SPEED_OFFSET;
    }

    // calculated the motor speed (deg/s) required to shoot a ball at the desired velocity (m/s)
    public double calculateMotorSpeed(double fireSpeed)
    {
        return 360 * fireSpeed / WHEEL_CIRCUMFERENCE;
    }

    public static void UpdateZOffset(double zoff){
        zoffset += zoff;
    }
    public static void UpdateYOffset(double yoff){
        yoffset += yoff;
    }

    public void UpdateFiring(FireInfo info)
    {
        if (isShooting) {
            angle = info.launchAngle;
//            shooterServoBig.setPosition(angle * R);
//            shooterServoSmall.setPosition(angle * R);
            shooterMotorSmall.setVelocity(calculateMotorSpeed(info.speed / 39.3700787402), AngleUnit.DEGREES);
//            shooterMotorBig.setVelocity(calculateMotorSpeed(FIRE_SPEED));
        }
    }

}
