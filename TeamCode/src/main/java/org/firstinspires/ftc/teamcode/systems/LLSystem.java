package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.common.Coordinate3D;
import org.firstinspires.ftc.teamcode.common.Hive;
import org.firstinspires.ftc.teamcode.common.HiveType;

import java.util.List;

public class LLSystem {

    final double UP_THRESHOLD = 47;
    private Hive[] hives;

    private Limelight3A lime_light;
    long lastUpdate;

    public LLSystem(Limelight3A limelight)
    {
        if (!limelight.isRunning())
            limelight.start();

        this.lime_light = limelight;
        hives = new Hive[4];
        lastUpdate = lime_light.getTimeSinceLastUpdate();
    }

    public boolean IsHiveUp(HiveType ht)
    {
        Hive h = hives[ht.ordinal()];
        Coordinate3D coords = h.CalculatePos();
        return coords.y > UP_THRESHOLD;
    }

    public void Detect()
    {
        // skip until new update
        if (lime_light.getTimeSinceLastUpdate() <= lastUpdate)
            return;

        LLResult result = lime_light.getLatestResult();
        if (!result.isValid())
            return;

        // on new loop, clear hives
        for (Hive hive : hives)
            hive.Clear();

        List<LLResultTypes.FiducialResult> results = result.getFiducialResults();

        // add april tags to their hives
        for (LLResultTypes.FiducialResult tag : results) {
            HiveType type = Hive.GetHiveType(tag.getFiducialId());
            hives[type.ordinal()].AddAprilTag(tag);
        }

    }

    public HiveType GetClosestHive()
    {
        double closest = 1000000; // big number
        HiveType h = null;
        for (int i=0; i<4; i++)
        {
            double dist = hives[i].CalculatePos().Length();
            if (dist < closest)
            {
                h = HiveType.values()[i];
                closest = dist;
            }
        }

        return h;
    }

    public Hive GetHive(HiveType hType)
    {
        return hives[hType.ordinal()];
    }
}
