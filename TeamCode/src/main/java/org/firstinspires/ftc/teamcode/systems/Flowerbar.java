public class Flowerbar { 
    Servo LeftFlowerbarServo;
    Servo RightFlowerbarServo;
    
    public Flowerbar(HardwareMap hardwareMap) { 
        LeftFlowerbarServo = hardwareMap.get(Servo.class, "left_flowerbar");
        RightFlowerbarServo = hardwareMap.get(Servo.class, "right_flowerbar");
       
       
    }
    public void Lowerarmbar() { 
        LeftFlowerbarServo.setPosition(0.0);
        RightFlowerbarServo.setPosition(1.0);
    
       
    }
    public void Raisearmbar() { 
        LeftFlowerbarServo.setPosition(1.0);
        RightFlowerbarServo.setPosition(0.0);
    
       
    }
}
    

