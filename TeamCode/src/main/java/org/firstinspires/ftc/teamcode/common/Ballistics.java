package org.firstinspires.ftc.teamcode.common;

import org.firstinspires.ftc.teamcode.systems.Shooter;

public class Ballistics
{
    static final double STARTING_FIRING_SPEED = 3.0;

    public static double GetFiringAngle(Coordinate3D goalPos, double roboHeading, double firingSpeed)
    {

        Coordinate3D relativePos = Matrix.RotateCoord(goalPos, 0, roboHeading, 0);

        relativePos.x += Shooter.LL_OFFSET.x;
        relativePos.y += Shooter.LL_OFFSET.y + Shooter.getYOffset();
        relativePos.z += Shooter.LL_OFFSET.z + Shooter.getZOffset();

        double discriminant = Math.pow(firingSpeed,4)-19.6 *Math.pow(firingSpeed, 2)*relativePos.y-96.04*Math.pow(relativePos.y, 2);
        double answer = (Math.pow(firingSpeed, 2) - Math.sqrt(discriminant))/(9.8* relativePos.z);
        return Math.atan(answer);
    }

    public static double GetFiringAngleInch(Coordinate3D goalPos, double roboHeading, double firingSpeed)
    {
        goalPos.x /= 39.3700787402;
        goalPos.y /= 39.3700787402;
        goalPos.z /= 39.3700787402;
        return GetFiringAngle(goalPos, roboHeading, firingSpeed);
    }

    // firing speed, firing angle
    public static FireInfo GetFireInfo(Coordinate3D goalPos, double roboHeading, double minimumRange)
    {
        FireInfo fireInfo = new FireInfo();
        fireInfo.launchAngle = 0;
        fireInfo.speed = 0;

        // code that does stuff
        double CurrentFiringSpeed = STARTING_FIRING_SPEED;
        double angle = GetFiringAngleInch(goalPos, roboHeading, STARTING_FIRING_SPEED);
        if(minimumRange > angle) {
            while(minimumRange > angle) {
                CurrentFiringSpeed /= 2; 
                angle = GetFiringAngleInch(goalPos, roboHeading, CurrentFiringSpeed);
            }


        }
        fireInfo.speed = CurrentFiringSpeed;
        fireInfo.launchAngle = angle;

        return fireInfo;
    
    
    }

     
    public static double[] GetVelocityOffset(double goalY, double velocityX, double velocityY, double targetvelocityY){
        double[] coordinate = {0,0};

        double goalHeightM = Shooter.goal_height / 39.3700787402;

//        d=v*t*1/2*a*t^2;
        double time = (-targetvelocityY - Math.sqrt(targetvelocityY*targetvelocityY - 4 * -goalHeightM * 4.9))/-9.8;


        double distanceX = (-time)*velocityX;
        double distanceY = (-time)*velocityY;

        coordinate[0] = distanceX;
        coordinate[1] = distanceY;

        return coordinate;
    }

    public static FireInfo GetFireInfoVelocity(Coordinate3D goalPos, Coordinate3D velocity, double roboHeading, double firingSpeed)
    {
        double angle = GetFiringAngle(goalPos, roboHeading, firingSpeed);

        double targetVelocityY = Math.sin(angle * Math.PI/180);
        double[] offsets = GetVelocityOffset(goalPos.z, velocity.x, velocity.z, targetVelocityY);
        goalPos.x += offsets[0];
        goalPos.z += offsets[1];
        angle = GetFiringAngle(goalPos, roboHeading, firingSpeed);

        FireInfo info = new FireInfo();
        info.launchAngle=angle;
        info.speed = firingSpeed;
        info.pointToLookAt = goalPos;
        return info;
    }

    public static FireInfo GetFireInfoWithVelocityInch(Coordinate3D goalPos, Coordinate3D velocity, double roboHeading, double firingSpeed)
    {
        goalPos.x /= 39.3700787402;
        goalPos.y /= 39.3700787402;
        goalPos.z /= 39.3700787402;

        velocity.x /= 39.3700787402;
        velocity.y /= 39.3700787402;
        velocity.z /= 39.3700787402;

        return GetFireInfoVelocity(goalPos, velocity, roboHeading, firingSpeed);
    }



}