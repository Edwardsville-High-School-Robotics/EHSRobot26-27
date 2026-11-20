package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Feeder {
    private DcMotorEx intakeMotor;
    private double IntakeMotorSpeed = 0.7;
    boolean isFeeding;

    public Feeder(HardwareMap map)
    {
        intakeMotor = map.get(DcMotorEx.class, "artifact_feeder");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void StartFeeding()
    {
        intakeMotor.setPower(IntakeMotorSpeed);
        isFeeding = true;
    }

    public void StopFeeding()
    {
        intakeMotor.setPower(0);
        isFeeding = false;
    }

    public boolean IsFeeding()
    {
        return isFeeding;
    }

    public void Reverse()
    {
        intakeMotor.setDirection(intakeMotor.getDirection().inverted());
        intakeMotor.setPower(IntakeMotorSpeed);
    }
}
