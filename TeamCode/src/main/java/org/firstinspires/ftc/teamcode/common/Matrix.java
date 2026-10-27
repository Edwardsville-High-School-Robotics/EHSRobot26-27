package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.limelightvision.LLResultTypes;


public class Matrix {

    static public Coordinate3D RotateCoord(Coordinate3D coordinate, LLResultTypes.FiducialResult result)
    {
        double cosa = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getPitch());
        double cosb = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getYaw());
        double cosc = Math.cos(result.getTargetPoseRobotSpace().getOrientation().getRoll());
        double sina = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getPitch());
        double sinb = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getYaw());
        double sinc = Math.sin(result.getTargetPoseRobotSpace().getOrientation().getRoll());

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
