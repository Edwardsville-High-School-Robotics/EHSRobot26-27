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

    public static final Pose start = poseFactory.of(82.3533, 132.9418, 270);
    public static final Pose point1 = poseFactory.of(132.9418, 48.8828, 180);
    public static final Pose point1Control1 = poseFactory.of(123.291, 98.7049, 0);

    public static Path path1() {
        return Paths.curve(start, point1Control1, point1).linear(start, point1);
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
                follow(follower, path1())
        ), llTask);
    }
}