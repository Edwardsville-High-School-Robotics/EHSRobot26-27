package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

// Limelight robot space (as observed): X = right, Y = down, Z = forward.
public class Matrix {

    static double[][] Rx(double t) {
        double c = Math.cos(t), s = Math.sin(t);
        return new double[][]{{1, 0, 0}, {0, c, -s}, {0, s, c}};
    }

    static double[][] Ry(double t) {
        double c = Math.cos(t), s = Math.sin(t);
        return new double[][]{{c, 0, s}, {0, 1, 0}, {-s, 0, c}};
    }

    static double[][] Rz(double t) {
        double c = Math.cos(t), s = Math.sin(t);
        return new double[][]{{c, -s, 0}, {s, c, 0}, {0, 0, 1}};
    }

    static double[][] Mul(double[][] a, double[][] b) {
        double[][] r = new double[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    r[i][j] += a[i][k] * b[k][j];
        return r;
    }

    static Coordinate3D Apply(double[][] m, Coordinate3D v) {
        return new Coordinate3D(
                m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z,
                m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z,
                m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z);
    }

    // Rotation built from angles about X, Y, Z, in radians.
    // Order unverified: if per-tag estimates spread apart when the robot turns,
    // try Mul(Rx, Mul(Ry, Rz)) or Mul(Rz, Mul(Ry, Rx)) here.
    static double[][] Rotation(double rx, double ry, double rz) {
        return Mul(Ry(ry), Mul(Rx(rx), Rz(rz)));
    }

    // Generic version, angles in degrees about X (right), Y (down), Z (forward).
    static public Coordinate3D RotateCoord(Coordinate3D coordinate, double rxDeg, double ryDeg, double rzDeg) {
        return Apply(Rotation(Math.toRadians(rxDeg), Math.toRadians(ryDeg), Math.toRadians(rzDeg)), coordinate);
    }

    // Rotates a tag-frame offset into robot space using the tag's measured orientation.
    static public Coordinate3D RotateCoord(Coordinate3D coordinate, LLResultTypes.FiducialResult result) {
        YawPitchRollAngles o = result.getTargetPoseRobotSpace().getOrientation();
        // FTC labels don't match the axes: roll = about X, pitch = about Y, yaw = about Z
        return Apply(Rotation(
                o.getRoll(AngleUnit.RADIANS),
                o.getPitch(AngleUnit.RADIANS),
                o.getYaw(AngleUnit.RADIANS)), coordinate);
    }
}