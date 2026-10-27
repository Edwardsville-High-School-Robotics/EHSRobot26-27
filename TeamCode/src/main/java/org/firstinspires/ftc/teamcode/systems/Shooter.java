package org.firstinspires.ftc.teamcode.systems;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Ballistics;
import org.firstinspires.ftc.teamcode.common.HiveType;

public class Shooter {
    final double FIRE_SPEED = 4;
    private boolean isShooting;

    double goal_height = 54;

    Pose2D[] hive_positions = {
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // red aud
            new Pose2D(DistanceUnit.INCH, 57.1, 85, AngleUnit.DEGREES, 0), // red field
            new Pose2D(DistanceUnit.INCH, 84.4, 55, AngleUnit.DEGREES, 0), // blue aud
            new Pose2D(DistanceUnit.INCH, 84.4, 85, AngleUnit.DEGREES, 0)}; // blue field

    public Shooter()
    {
    }

    private double GetAngle(Pose2D currentPos, HiveType target)
    {
        Pose2D goalPos = hive_positions[target.ordinal()];
        // distance is x
        double x = Math.sqrt(Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2) +
                Math.pow(goalPos.getX(DistanceUnit.INCH) - currentPos.getX(DistanceUnit.INCH), 2));
        return Ballistics.GetFiringAngle(x, goal_height, FIRE_SPEED);
    }

    public void Shoot()
    {

    }

    public boolean IsShooting()
    {
        return isShooting;
    }
}
