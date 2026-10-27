package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static java.util.concurrent.locks.LockSupport.park;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class RedAudienceAuto {

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    private static final Pose start = poseFactory.of(58.1274, 4.1954, 90);
    private static final Pose point1 = poseFactory.of(15.1981, 3.5593, 0.8489);
    private static final Pose point2 = poseFactory.of(58.0315, 4.887, -178.2246);
    private static final Pose point3 = poseFactory.of(7.8611, 100.513, -62.3162);

    public static Path feed1() {
        return Paths.line(start, point1).reverseTangent();
    }

    public static Path park2() {
        return Paths.line(point1, point3).reverseTangent();
    }

    public static Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder, LLSystem llsystem) {
        Command shootCommand = Command.build().setStart(shooter::Shoot).setDone(()->{return !shooter.IsShooting();});
        Command startFeeding = Command.build().setStart(feeder::StartFeeding).setDone(()->{return true;});
        Command stopFeeding = Command.build().setStart(feeder::StopFeeding).setDone(()->{return true;});

        return sequential(
                shootCommand,
                startFeeding,
                follow(follower, feed1()),
                stopFeeding,
                follow(follower, park2())
        );
    }
}