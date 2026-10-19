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
        List<LLResultTypes.FiducialResult> Results;
        ArrayList<Integer> IDS = new ArrayList<Integer>();
        if (limelight.isConnected()) {

            LLResult result = limelight.getLatestResult();
            Results = result.getFiducialResults();
            for(int i = 0; i < Results.size(); i++)
            {
                IDS.add(Results.get(i).getFiducialId());
            }
            if(IDS.size() != 4)
            {
                if(IDS.size() < 4)
                {

                }
                else{

                }
            }
            int BLUE_FIELD_COUNT = 0;
            int RED_FIELD_COUNT = 0;
            int RED_AUDIENCE_COUNT = 0;
            int BLUE_AUDIENCE_COUNT = 0;

            for(int i = 0; i < IDS.size(); i++)
            {
                for(int j = 0; j < 4; j++)
                {
                    if(IDS.get(i) == RED_AUDIENCE[j])
                    {
                        RED_AUDIENCE_COUNT++;
                    }
                    else if(IDS.get(i) == RED_FIELD[j])
                    {
                        RED_FIELD_COUNT++;
                    }
                    else if(IDS.get(i) == BLUE_AUDIENCE[j])
                    {
                        BLUE_AUDIENCE_COUNT++;
                    }
                    else if(IDS.get(i) == BLUE_FIELD[j])
                    {
                        BLUE_FIELD_COUNT++;
                    }
                }
            }
            if(BLUE_AUDIENCE_COUNT > BLUE_FIELD_COUNT && BLUE_AUDIENCE_COUNT > RED_AUDIENCE_COUNT && BLUE_AUDIENCE_COUNT > RED_FIELD_COUNT)
            {
                temp = BLUE_AUDIENCE;
            }
            else if(BLUE_FIELD_COUNT > BLUE_AUDIENCE_COUNT && BLUE_FIELD_COUNT > RED_AUDIENCE_COUNT && BLUE_FIELD_COUNT > RED_FIELD_COUNT)
            {
                temp = BLUE_FIELD;
            }
            else if(RED_AUDIENCE_COUNT > BLUE_FIELD_COUNT && RED_AUDIENCE_COUNT > BLUE_AUDIENCE_COUNT && RED_AUDIENCE_COUNT > RED_FIELD_COUNT)
            {
                temp = RED_AUDIENCE;
            }
            else if(RED_FIELD_COUNT > BLUE_FIELD_COUNT && RED_FIELD_COUNT > RED_AUDIENCE_COUNT && RED_FIELD_COUNT > BLUE_AUDIENCE_COUNT)
            {
                temp = RED_FIELD;
            }

        }
    }
}
