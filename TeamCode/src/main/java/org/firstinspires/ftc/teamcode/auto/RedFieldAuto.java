package org.firstinspires.ftc.teamcode.auto;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import static java.util.concurrent.locks.LockSupport.park;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.common.HiveType;
import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class RedFieldAuto {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(60, 132, 270);
    private final Pose path1 = poseFactory.of(12, 120, 0);
    private final Pose path1Control1 = poseFactory.of(60, 110, 0);
    public Path path1() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }
    public Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder, LLSystem llSystem) {
        Command shootCommand = Command.build().setStart(shooter::Shoot).setDone(()->{return !shooter.IsShooting();});
        Command startFeeding = Command.build().setStart(feeder::StartFeeding).setDone(()->{return true;});
        Command stopFeeding = Command.build().setStart(feeder::StopFeeding).setDone(()->{return true;});

        return sequential(
                // shoot
                race(sequential(waitUntil(() -> {return llSystem.IsHiveUp(HiveType.RED_FIELD);}), shootCommand), waitMs(20000)),
                follow(follower, path1())
        );
    }
}
