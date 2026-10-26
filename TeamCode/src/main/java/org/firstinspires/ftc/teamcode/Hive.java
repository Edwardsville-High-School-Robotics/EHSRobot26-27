package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.ArrayList;

enum HiveType
{
    RED_FIELD,
    RED_AUDIENCE,
    BLUE_FIELD,
    BLUE_AUDIENCE
}

public class Hive {



    // inches
    final double[] xOffsets = {-6,-3,3,6};
    final double yOffset = 5.5;
    final double zOffset = 7.5;
    final int[] RED_FIELD = {30,31,32,33};
    final int[] RED_AUDIENCE = {34,35,36,37};
    final int[] BLUE_FIELD = {38, 39, 40, 41};
    final int[] BLUE_AUDIENCE = {42,43,44,45};

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

    public Coordinate3D CalculatePos()
    {
        Coordinate3D pose = new Coordinate3D(0,0,0);

        for (int i = 0; i < 4; i++)
        {
//            aprilTags[i].
            Pose3D tagPose = aprilTags[i].getTargetPoseRobotSpace();
            tagPose.getOrientation();

        }
        return pose;
    }

}
