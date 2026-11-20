package org.firstinspires.ftc.teamcode.systems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;
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
    public static final double FIRE_ANGLE_POLLEN = 83.56;
    public static final double FIRE_ANGLE_NECTAR = 83.56; // 81.5
    final double WHEEL_CIRCUMFERENCE = 0.096 * Math.PI;
    public static double FIRE_MOTOR_SPEED_OFFSET = 1.29;
    public static double MOTOR_SPEED_THRESHOLD = 0.85;
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

    FireInfo curPollen, curNectar;
    public Shooter(HardwareMap map)
    {
        shooterMotorSmall = map.get(DcMotorEx.class, "pollen_shooter");
        shooterMotorSmall.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotorBig = map.get(DcMotorEx.class, "nectarShooter");
        shooterMotorBig.setDirection(DcMotorSimple.Direction.FORWARD);

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

    public boolean IsTimerElapsed()
    {
        return shooterTimer.seconds() > SHOOT_TIME;
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
        shooterMotorBig.setVelocity(0);
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

    public CommandBuilder SpinUpCommand(Follower follower, LLSystem llsystem, HiveType htype)
    {
        return Command.build().setStart(this::StartShooting).setExecute(() -> {

                    Coordinate3D hivePos = llsystem.GetHive(htype).CalculatePos(follower.pose().heading());
                    if (hivePos == null)
                        return;

                    curPollen = Ballistics.GetFireInfoWithVelocityInch(hivePos,
                            new Coordinate3D(follower.velocity().vy, 0, follower.velocity().vx),
                            0,
                            Shooter.FIRE_ANGLE_POLLEN,
                            Ballistics.SPIN_K_POLLEN);

                    this.UpdateFiring(curPollen,null);})
                .setDone(() -> {
                        if (shooterTimer.seconds() > 2.0) return true;   // give up and move on
                        return curPollen != null
                                && shooterMotorSmall.getVelocity(AngleUnit.DEGREES)
                                >= calculateMotorSpeed(curPollen.speed / 39.3700787402) * MOTOR_SPEED_THRESHOLD;
                    });
    }
    public CommandBuilder GetCommand(Follower follower, LLSystem llsystem, HiveType htype)
    {
        return Command.build().setStart(this::StartShooting).setExecute(() -> {

                    Coordinate3D hivePos = llsystem.GetHive(htype).CalculatePos(follower.pose().heading());
                    if (hivePos == null)
                        return;

                    FireInfo pollen = Ballistics.GetFireInfoWithVelocityInch(hivePos,
                            new Coordinate3D(follower.velocity().vy, 0, follower.velocity().vx),
                            0,
                            Shooter.FIRE_ANGLE_POLLEN,
                            Ballistics.SPIN_K_POLLEN);

                    this.UpdateFiring(pollen,null);})
                .setDone(this::IsTimerElapsed).setEnd((EndCondition cond) -> {
                    this.StopShooting();
                });
    }
    public void UpdateFiring(FireInfo pollen, FireInfo nectar)
    {
        if (isShooting) {
//            shooterServoBig.setPosition(angle * R);
//            shooterServoSmall.setPosition(angle * R);
            if (pollen != null)
                shooterMotorSmall.setVelocity(calculateMotorSpeed(pollen.speed / 39.3700787402), AngleUnit.DEGREES);
            if (nectar != null)
                shooterMotorBig.setVelocity(calculateMotorSpeed(nectar.speed / 39.3700787402), AngleUnit.DEGREES);
//            shooterMotorBig.setVelocity(calculateMotorSpeed(FIRE_SPEED));
        }
    }

}
