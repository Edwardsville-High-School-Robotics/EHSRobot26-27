package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class BlueFieldAuto {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(81.7556, 137.7223, 90);
    private final Pose point1 = poseFactory.of(133.6389, 128.813, 170.2564);
    private final Pose point2 = poseFactory.of(80.7167, 115.1556, 14.4704);
    private final Pose point3 = poseFactory.of(100.6222, 109.4223, 163.9324);
    private final Pose point4 = poseFactory.of(137.7019, 36.8981, 117.0794);

    public static Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder, LLSystem llsystem) {
        return sequential();
    }

    public Path path1() {
        return Paths.line(start, point1).reverseTangent();
    }

    public Path path2() {
        return Paths.line(point1, point2).reverseTangent();
    }

    public Path path3() {
        return Paths.line(point2, point3).reverseTangent();
    }

    public Path path4() {
        return Paths.line(point3, point4).reverseTangent();
    }
}