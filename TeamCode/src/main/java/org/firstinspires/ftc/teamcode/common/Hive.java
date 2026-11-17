package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.systems.LLSystem;

public class Hive {



    // inches
    // coordinates are (field x, field y, height)
    // limelight convention is (leftright, -height, forward/back) for some reason
    final double[] xOffsets = {-6,-3,3,6};
    final double zOffset = 5.5;
    final double yOffset = -7.5;

    final int[] RED_FIELD = {30,31,32,33};
    final int[] RED_AUDIENCE = {34,35,36,37};
    final int[] BLUE_AUDIENCE = {38, 39, 40, 41};
    final int[] BLUE_FIELD = {42,43,44,45};

    private LLResultTypes.FiducialResult[] aprilTags;
    private int numTags;

    public static HiveType GetHiveType(int id)
    {
        return HiveType.values()[(id+2) / 4 - 8];
    }
    private static int GetIndex(int id)
    {
        return (id + 2) % 4;
    }

    public Hive()
    {
        aprilTags = new LLResultTypes.FiducialResult[4];
        numTags = 0;
    }

    public void AddAprilTag(LLResultTypes.FiducialResult tag)
    {
        int id = tag.getFiducialId();
        aprilTags[GetIndex(id)] = tag;
        numTags++;
        assert numTags <= 4;
    }

    public int GetNumTags()
    {
        return numTags;
    }

    public void Clear()
    {
        numTags = 0;
        for (int i = 0; i < 4; i++)
            aprilTags[i] = null;
    }

    public Coordinate3D CalculatePos(double robotRotation)
    {
        Coordinate3D pose = new Coordinate3D(0,0,0);


        for (int i = 0; i < 4; i++)
        {
          if (aprilTags[i] == null)
                continue;
            Pose3D tagPose = aprilTags[i].getTargetPoseRobotSpace();

            Coordinate3D offset = new Coordinate3D(-xOffsets[i], yOffset, zOffset);
            Coordinate3D globalFireOffset = Matrix.RotateCoord(offset, aprilTags[i]);
            Position position = tagPose.getPosition().toUnit(DistanceUnit.INCH);
            Coordinate3D estimateFirePos = new Coordinate3D(0,0,0);
            estimateFirePos.x = globalFireOffset.x + position.x;
            estimateFirePos.y = globalFireOffset.y + position.y;
            estimateFirePos.z = globalFireOffset.z + position.z;
//            estimateFirePos = Matrix.RotateCoord(estimateFirePos, -LLSystem.LL_Y_ANGLE_OFFSET, 0, 0);

            pose.x += estimateFirePos.x;
            pose.y += estimateFirePos.y;
            pose.z += estimateFirePos.z;
        }

        if (GetNumTags() == 0)
            return null;


        pose.x /= GetNumTags();
        pose.y /= GetNumTags();
        pose.z /= GetNumTags();

        pose = Matrix.RotateCoord(pose, 0, -robotRotation, 0);

        return pose;
    }

}
