package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

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

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    private static final Pose start = poseFactory.of(81.7556, 137.7223, 90);
    private static  final Pose point1 = poseFactory.of(133.6389, 128.813, 170.2564);
    private static final Pose point2 = poseFactory.of(80.7167, 115.1556, 14.4704);
    private static final Pose point3 = poseFactory.of(100.6222, 109.4223, 163.9324);
    private static final Pose point4 = poseFactory.of(137.7019, 36.8981, 117.0794);

    public static Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder, LLSystem llsystem) {
        Command shootCommand = Command.build().setStart(shooter::Shoot).setDone(()->{return !shooter.IsShooting();});
        Command startFeeding = Command.build().setStart(feeder::StartFeeding).setDone(()->{return true;});
        Command stopFeeding = Command.build().setStart(feeder::StopFeeding).setDone(()->{return true;});

        return sequential(
                //shootCommand,
                //startFeeding,
                //follow(follower, feed1()),
                //stopFeeding,
                follow(follower, park2())
        );
    }

    public static Path feed1() {
        return Paths.line(start, point1).reverseTangent();
    }

    //public static Path park2() {return Paths.line(point1, point3).reverseTangent();}
    public static Path park2() {
        return Paths.line(start, point3).reverseTangent();
    }
}