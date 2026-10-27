package org.firstinspires.ftc.teamcode.common;

public class Coordinate3D {

    public double x;
    public double y;
    public double z;

    public static double[] GetGoalPos(
            double angleX,
            double angleY,
            double distance) {

        double x = distance
                * Math.cos(Math.toRadians(angleY))
                * Math.cos(Math.toRadians(angleX));

        double y = distance
                * Math.cos(Math.toRadians(angleY))
                * Math.sin(Math.toRadians(angleX));

        double z = distance
                * Math.sin(Math.toRadians(angleY));

        return new double[] {x, y, z};
    }

    public Coordinate3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
