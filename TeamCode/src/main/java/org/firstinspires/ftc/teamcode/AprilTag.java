package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
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

        Hive hives[] = new Hive[4];

        ArrayList<Integer> IDS = new ArrayList<Integer>();
        while (opModeIsActive()){
            for (Hive h : hives)
            {
                h.Clear();
            }

            if (limelight.isConnected()) {

                LLResult result = limelight.getLatestResult();
                List<LLResultTypes.FiducialResult> results = result.getFiducialResults();

                for (LLResultTypes.FiducialResult tag : results) {
                    HiveType type = Hive.GetHiveType(tag.getFiducialId());
                    hives[type.ordinal()].AddAprilTag(tag);
                }
            }
        }
    }
}
