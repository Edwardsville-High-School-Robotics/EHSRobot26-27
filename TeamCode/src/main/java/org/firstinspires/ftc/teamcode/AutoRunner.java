package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class AutoRunner extends LinearOpMode {

    private Follower follower;


    @Override
    public void runOpMode() throws InterruptedException {

        boolean isRed = false;
        boolean isAudience = false;

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

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

        boolean isShooting = true;
        Scheduler.reset();
        Shooter greatAimer = new Shooter();
        follower = Constants.create(hardwareMap);



        waitForStart();
    }
}
