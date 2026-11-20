package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flowerbar {
    private Servo LeftFlowerbarServo;
    private Servo RightFlowerbarServo;
    private boolean isLowered;
    public boolean IsLowered()
    {
        return isLowered;
    }
    public Flowerbar(HardwareMap hardwareMap) { 
        LeftFlowerbarServo = hardwareMap.get(Servo.class, "left_flowerbar");
        RightFlowerbarServo = hardwareMap.get(Servo.class, "right_flowerbar");
        isLowered = false;
        Reset();
    }

    public void Reset()
    {
        LeftFlowerbarServo.setPosition(.86);
        RightFlowerbarServo.setPosition(0.14);
    }
    public void LowerArmbar() {
        LeftFlowerbarServo.setPosition(0.43);
        RightFlowerbarServo.setPosition(0.57);

       isLowered = true;
    }
    public void RaiseArmbar() {
        LeftFlowerbarServo.setPosition(0.75);
        RightFlowerbarServo.setPosition(0.25);
    
       isLowered = false;
    }
}
    

