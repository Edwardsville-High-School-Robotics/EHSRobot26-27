package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.common.Ballistics;
import org.firstinspires.ftc.teamcode.common.Coordinate3D;
import org.firstinspires.ftc.teamcode.common.FireInfo;
import org.firstinspires.ftc.teamcode.common.HiveType;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.LLSystem;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class BlueFieldAuto {

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    public static final Pose start = poseFactory.of(84, 136, 270);
    public static final Pose path1 = poseFactory.of(108, 136, 0);
    public static final Pose path1Control1 = poseFactory.of(96, 120, 0);
    public static final Pose point2 = poseFactory.of(135, 136, 0);
    public static final Pose point3Start = poseFactory.of(135, 136, 0);
    public static final Pose point3 = poseFactory.of(136, 51, 180);
    public static final Pose point3Control1 = poseFactory.of(108, 94, 0);

    public static Path OrientToFeed() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    public static Path Feed() {
        return Paths.line(path1, point2).tangent().with(Constants.foresightConfig.maxVelocityConstraint.at(6.0));
    }

    public static Path ToPark() {
        return Paths.curve(point3Start, point3Control1, point3).linear(point3Start, point3);
    }
    public static Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder, LLSystem llSystem) {
        Command aim = shooter.GetCommand(follower, llSystem, HiveType.BLUE_FIELD);
        Command spinupCommand = shooter.SpinUpCommand(follower, llSystem, HiveType.BLUE_FIELD);
        Command startFeeding = Command.build().setStart(feeder::StartFeeding).setDone(()->{return true;});
        Command stopFeeding = Command.build().setStart(feeder::StopFeeding).setDone(()->{return true;});
        Command llTask = Command.build().setExecute(llSystem::Detect).setDone(() -> {
            return false;
        });
        Command shoot = sequential(spinupCommand, startFeeding, aim).setEnd((EndCondition cond)->{feeder.StopFeeding();});

        return race(sequential(
                shoot,
                follow(follower, OrientToFeed()),
                startFeeding,
                follow(follower, Feed()),
                stopFeeding,
                follow(follower, ToPark())
        ), llTask);
    }
}