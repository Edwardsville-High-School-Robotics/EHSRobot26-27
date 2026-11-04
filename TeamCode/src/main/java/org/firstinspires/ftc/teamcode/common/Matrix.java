package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Matrix {

    static public Coordinate3D RotateCoord(Coordinate3D coordinate, double pitch, double yaw, double roll)
    {
        double cosa = Math.cos(pitch * Math.PI / 180);
        double cosb = Math.cos(yaw * Math.PI / 180);
        double cosc = Math.cos(roll * Math.PI / 180);
        double sina = Math.sin(pitch * Math.PI / 180);
        double sinb = Math.sin(yaw * Math.PI / 180);
        double sinc = Math.sin(roll* Math.PI / 180);

        double[][] matrix = new double[][]{{cosa * cosb, cosa * sinb * sinc - sina * cosc, cosa * sinb * cosc + sina * sinc},
                {sina * cosb, sina * sinb * sinc + cosa * cosc, sina * sinb * cosc - cosa * sinc},
                {-sina, cosb * sinc, cosb * cosc}};
        double[] coordinates = {coordinate.x, coordinate.y, coordinate.z};
        double[] resultant = new double[3];
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                resultant[j] += coordinates[k] * matrix[k][j];
            }
        }
        return new Coordinate3D(resultant[0], resultant[1], resultant[2]);
    }
    static public Coordinate3D RotateCoord(Coordinate3D coordinate, LLResultTypes.FiducialResult result)
    {
        double cosa = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getPitch(AngleUnit.RADIANS));
        double cosb = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getYaw(AngleUnit.RADIANS));
        double cosc = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getRoll(AngleUnit.RADIANS));
        double sina = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getPitch(AngleUnit.RADIANS));
        double sinb = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getYaw(AngleUnit.RADIANS));
        double sinc = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getRoll(AngleUnit.RADIANS));

        double[][] matrix = new double[][]{{cosa * cosb, cosa * sinb * sinc - sina * cosc, cosa * sinb * cosc + sina * sinc},
                {sina * cosb, sina * sinb * sinc + cosa * cosc, sina * sinb * cosc - cosa * sinc},
                {-sina, cosb * sinc, cosb * cosc}};
        double[] coordinates = {coordinate.x, coordinate.y, coordinate.z};
        double[] resultant = new double[3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    resultant[j] += coordinates[k] * matrix[k][j];
                }
            }
        return new Coordinate3D(resultant[0], resultant[1], resultant[2]);
    }



}
