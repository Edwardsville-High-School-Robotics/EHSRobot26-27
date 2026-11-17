package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.Coordinate3D;
import org.firstinspires.ftc.teamcode.common.Hive;
import org.firstinspires.ftc.teamcode.common.HiveType;

import java.util.List;

public class LLSystem {

    final double UP_THRESHOLD = 47; // the height a hive has to be considered up
    public static final double LL_Y_ANGLE_OFFSET = 55; // limelight is angled up 55 degrees

    private Hive[] hives;

    private GoBildaPinpointDriver pinpoint;
    private Limelight3A lime_light;
    long lastUpdate, currUpdate;

    Telemetry telemetry;

    public LLSystem(Limelight3A limelight, GoBildaPinpointDriver pinpoint, Telemetry telemetry)
    {
        this.telemetry = telemetry;
        this.pinpoint = pinpoint;

        if (!limelight.isRunning())
            limelight.start();

        this.lime_light = limelight;
        hives = new Hive[4];
        for (int i = 0; i < hives.length; i++)
        {
            hives[i] = new Hive();
        }
        lastUpdate = lime_light.getTimeSinceLastUpdate();
        currUpdate = lastUpdate;
    }
    public double GetRotation()
    {
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }

    public boolean IsHiveUp(HiveType ht)
    {
        Hive h = hives[ht.ordinal()];
        Coordinate3D coords = h.CalculatePos(pinpoint.getHeading(AngleUnit.DEGREES));
        return coords.y > UP_THRESHOLD;
    }

    public void Detect() {
        LLResult result = lime_light.getLatestResult();
        for (Hive hive : hives) hive.Clear();
        if (result == null || !result.isValid())
            return;  // no fresh data: all hives empty, so isDetected becomes false

        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
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
            Coordinate3D coord = hives[i].CalculatePos(GetRotation());
            if (coord == null)
                continue;

//            telemetry.addData("Hive: " + HiveType.values()[i].toString(), "Coord: " + coord.toString());

            double dist = coord.Length();
            if (dist < closest)
            {
                h = HiveType.values()[i];
                closest = dist;

//                telemetry.addData("Closest Hive", "Closest: " + h.toString() + " Dist: " + dist);
            }
        }

        return h;
    }

    public Hive GetHive(HiveType hType)
    {
        return hives[hType.ordinal()];
    }
}
