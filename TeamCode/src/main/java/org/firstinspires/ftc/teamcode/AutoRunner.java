package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auto.BlueAudienceAuto;
import org.firstinspires.ftc.teamcode.auto.BlueFieldAuto;
import org.firstinspires.ftc.teamcode.auto.RedAudienceAuto;
import org.firstinspires.ftc.teamcode.auto.RedFieldAuto;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.Flowerbar;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

@Autonomous(preselectTeleOp = "TeleopLL")
public class AutoRunner extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        boolean isRed = false;
        boolean isAudience = false;
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        Flowerbar flowerbar = new Flowerbar(hardwareMap);
        Follower follower = Constants.create(hardwareMap);
        Feeder feeder = new Feeder(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap);
        LLSystem llsystem = new LLSystem(limelight, pinpoint, telemetry);

        while (!isStarted())
        {
            if (gamepad1.b && !isRed) // b is red alliance (because b is red button)
            {
                isRed = true;
            }
            else if (gamepad1.x && isRed)
            {
                isRed = false;
            }

            if (gamepad1.y && !isAudience) {
                isAudience = true;
            }
            else if (gamepad1.a && isAudience)
            {
                isAudience = false;
            }

            telemetry.addData("Current Config", "IsRed = " + isRed + "\nIsAudience = " + isAudience);
            telemetry.update();
        }

        Command autoRoutine = null;
        Pose start = null;

        if (isRed)
        {
            if (isAudience)
            {
                autoRoutine = RedAudienceAuto.autoRoutine(follower, shooter, feeder, llsystem);
                start = RedAudienceAuto.start;
            }
            else
            {
                autoRoutine = RedFieldAuto.autoRoutine(follower, shooter, feeder, llsystem);
                start = RedFieldAuto.start;
            }
        }
        else
        {
            if (isAudience)
            {
                autoRoutine = BlueAudienceAuto.autoRoutine(follower, shooter, feeder, llsystem);
                start = BlueAudienceAuto.start;

            }
            else
            {
                autoRoutine = BlueFieldAuto.autoRoutine(follower, shooter, feeder, llsystem);
                start = BlueFieldAuto.start;
            }
        }
        follower.setPose(start);
        follower.update();

        Scheduler.reset();

        waitForStart();
        autoRoutine.schedule();
        flowerbar.RaiseArmbar();

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }
}
