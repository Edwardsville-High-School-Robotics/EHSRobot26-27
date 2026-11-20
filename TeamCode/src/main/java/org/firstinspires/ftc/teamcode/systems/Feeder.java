package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Feeder {
    private DcMotorEx intakeMotor;
    private double IntakeMotorSpeed = 0.7;
    boolean isFeeding;
    boolean isReversed;

    public Feeder(HardwareMap map)
    {
        intakeMotor = map.get(DcMotorEx.class, "artifact_feeder");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        isFeeding = false;
        isReversed = false;
    }

    public void StartFeeding()
    {
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setPower(IntakeMotorSpeed);
        isFeeding = true;
        isReversed = false;
    }

    public void StopFeeding()
    {
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setPower(0);
        isFeeding = false;
        isReversed = false;
    }

    public boolean IsFeeding()
    {
        return isFeeding;
    }

    public boolean IsReverse()
    {
        return isReversed;
    }

    public void Reverse()
    {
        isReversed = true;
        isFeeding = false;
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setPower(IntakeMotorSpeed);
    }
    public void StopReverse()
    {
        isReversed = false;
        isFeeding = false;
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setPower(0);
    }
}
