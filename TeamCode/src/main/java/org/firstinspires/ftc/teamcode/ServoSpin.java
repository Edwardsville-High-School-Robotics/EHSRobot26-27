package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class ServoSpin extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CRServo servo = hardwareMap.get(CRServo.class, "servo");

        double speed = 1;
        double resolution = .1;

        waitForStart();

        while (opModeIsActive())
        {
            if (gamepad1.dpadUpWasPressed())
            {
                resolution *= 10;
            }
            if (gamepad1.dpadDownWasPressed())
            {
                resolution /= 10;
            }

            if (gamepad1.dpadRightWasPressed())
            {
                speed += resolution;
            }
            if (gamepad1.dpadLeftWasPressed())
            {
                speed -= resolution;
            }

            resolution = Range.clip(resolution, 0, 1);
            speed = Range.clip(speed, -1, 1);

            servo.setPower(speed);

            telemetry.addData("servo", "Power: " + speed);
            telemetry.addData("servo", "Resolution: " + resolution);
            telemetry.update();
        }
    }
}
