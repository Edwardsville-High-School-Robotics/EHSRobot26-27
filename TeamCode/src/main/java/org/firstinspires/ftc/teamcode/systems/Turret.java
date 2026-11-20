package org.firstinspires.ftc.teamcode.systems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.CRServo;
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
import org.firstinspires.ftc.teamcode.common.Hive;
import org.firstinspires.ftc.teamcode.common.HiveType;

public class Turret {
    public static final double FIRE_SPEED = 4;
    final double WHEEL_CIRCUMFERENCE = 0.096 * Math.PI;
    public static double FIRE_MOTOR_SPEED_OFFSET = 1.13;
    public static double MOTOR_SPEED_THRESHOLD = 0.85;
    double SHOOT_TIME = 3.0; // run shoot for 3 seconds
    private boolean isShooting;
    private Timer shooterTimer; // times how long we've been shooting

    DcMotorEx shooterMotor;
    Servo pitchServo;
    CRServo yawServo;
    double R = 10;
    static double zoffset = 0;
    static double yoffset = 0;
    private boolean isRed;

    public static final Coordinate3D LL_OFFSET = new Coordinate3D(0,0,0); // offset of the shooter from the camera's position

    FireInfo curPollen, curNectar;
    Follower follower;
    LLSystem llSystem;

    public Turret(HardwareMap map, Follower follower, LLSystem llSystem, boolean isRed)
    {

        shooterMotor = map.get(DcMotorEx.class, "shooter_motor");
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        pitchServo = map.get(Servo.class, "pitch");
        yawServo = map.get(CRServo.class, "yaw");
        pitchServo.setDirection(Servo.Direction.FORWARD);
        yawServo.setDirection(CRServo.Direction.FORWARD);

        isShooting = false;
        this.isRed = isRed;
        this.follower = follower;
        this.llSystem = llSystem;

        shooterTimer = new Timer();
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
        shooterMotor.setVelocity(0);
    }

    // calculated the motor speed (deg/s) required to shoot a ball at the desired velocity (m/s)
    public double calculateMotorSpeed(double fireSpeed)
    {
        return 360 * fireSpeed / WHEEL_CIRCUMFERENCE;
    }

    public CommandBuilder SpinUpCommand(Follower follower, LLSystem llsystem, HiveType htype)
    {
        return Command.build().setStart(this::StartShooting).setExecute(() -> {
                    this.UpdateFiring();})
                .setDone(() -> {
                    return this.shooterMotor.getVelocity(AngleUnit.DEGREES) >= calculateMotorSpeed(curPollen.speed / 39.3700787402) * MOTOR_SPEED_THRESHOLD;
                });
    }
    public CommandBuilder GetCommand(Follower follower, LLSystem llsystem, HiveType htype)
    {
        return Command.build().setStart(this::StartShooting).setExecute(() -> {
                    this.UpdateFiring();})
                .setDone(this::IsTimerElapsed).setEnd((EndCondition cond) -> {
                    this.StopShooting();
                });
    }
    public void UpdateFiring()
    {
        if (isShooting) {
            Hive audience, field;

            if (isRed)
            {
                audience = llSystem.GetHive(HiveType.RED_AUDIENCE);
                field = llSystem.GetHive(HiveType.RED_FIELD);
            } else {
                audience = llSystem.GetHive(HiveType.BLUE_AUDIENCE);
                field = llSystem.GetHive(HiveType.BLUE_FIELD);
            }

            double heading = follower.pose().heading();
            Hive target = audience.IsUp(heading) ? audience : field;

            Coordinate3D hivePos = target.CalculatePos(heading);

            if (hivePos == null)
                return;

            Coordinate3D velocity = new Coordinate3D(follower.velocity().vy, 0, follower.velocity().vx);
//            curPollen = Ballistics.GetFireInfoWithVelocityInch(hivePos, velocity, heading, );

//            shooterServoBig.setPosition(angle * R);
//            shooterServoSmall.setPosition(angle * R);
            if (curPollen != null)
                shooterMotor.setVelocity(calculateMotorSpeed(curPollen.speed / 39.3700787402), AngleUnit.DEGREES);
//            if (nectar != null)
//                shooterMotorBig.setVelocity(calculateMotorSpeed(nectar.speed / 39.3700787402), AngleUnit.DEGREES);
//            shooterMotorBig.setVelocity(calculateMotorSpeed(FIRE_SPEED));
        }
    }

}
