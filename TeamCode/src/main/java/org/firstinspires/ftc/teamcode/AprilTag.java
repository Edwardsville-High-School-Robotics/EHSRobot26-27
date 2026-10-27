package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.Coordinate3D;
import org.firstinspires.ftc.teamcode.common.Hive;
import org.firstinspires.ftc.teamcode.common.HiveType;

import java.util.List;

public class AprilTag extends LinearOpMode {
    final int[] RED_FIELD = {30,31,32,33};
    final int[] RED_AUDIENCE = {34,35,36,37};
    final int[] BLUE_FIELD = {38, 39, 40, 41};
    final int[] BLUE_AUDIENCE = {42,43,44,45};
    int[] temp = {0,0,0,0};


    Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");
    @Override
    public void runOpMode() {
        limelight.start();
        waitForStart();

        boolean isRed = false;

        Hive hives[] = new Hive[4];

        while (opModeIsActive()){
            for (Hive h : hives)
            {
                h.Clear();
            }

            if (limelight.isConnected()) {

                LLResult result = limelight.getLatestResult();
                List<LLResultTypes.FiducialResult> results = result.getFiducialResults();
                Coordinate3D estimatedFirePos;

                for (LLResultTypes.FiducialResult tag : results) {
                    HiveType type = Hive.GetHiveType(tag.getFiducialId());
                    hives[type.ordinal()].AddAprilTag(tag);
                }

                HiveType selectedHive;
                if (isRed) {
                    selectedHive = HiveType.RED_FIELD;
                }
                else {
                    selectedHive = HiveType.BLUE_FIELD;
                }

                if (hives[selectedHive.ordinal()].GetNumTags() == 0)
                {
                    selectedHive = HiveType.values()[selectedHive.ordinal()+1];
                }

                if (hives[selectedHive.ordinal()].GetNumTags() == 0)
                {
                    continue;
                }

                Coordinate3D firePos = hives[selectedHive.ordinal()].CalculatePos();
            }
        }
    }
}
