package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Auto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        boolean isRed = false;
        boolean isAudience = false;

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

        Command autoCommand = Command.build();

        waitForStart();
        autoCommand.start();
    }
}
