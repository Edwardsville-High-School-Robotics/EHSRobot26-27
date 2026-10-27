package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.BlueAudienceAuto;
import org.firstinspires.ftc.teamcode.auto.BlueFieldAuto;
import org.firstinspires.ftc.teamcode.auto.RedAudienceAuto;
import org.firstinspires.ftc.teamcode.auto.RedFieldAuto;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class AutoRunner extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        boolean isRed = false;
        boolean isAudience = false;

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        Follower follower = Constants.create(hardwareMap);
        Shooter shooter = new Shooter();
        Feeder feeder = new Feeder();
        LLSystem llsystem = new LLSystem(limelight);

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

        if (isRed)
        {
            if (isAudience)
            {
                autoRoutine = RedAudienceAuto.autoRoutine(follower, shooter, feeder, llsystem);
            }
            else
            {
                autoRoutine = RedFieldAuto.autoRoutine(follower, shooter, feeder, llsystem);
            }
        }
        else
        {
            if (isAudience)
            {
                autoRoutine = BlueAudienceAuto.autoRoutine(follower, shooter, feeder, llsystem);
            }
            else
            {
                autoRoutine = BlueFieldAuto.autoRoutine(follower, shooter, feeder, llsystem);
            }
        }

        Scheduler.reset();

        waitForStart();
        autoRoutine.schedule();

        while (opModeIsActive())
        {
            Scheduler.execute();
        }
    }
}
