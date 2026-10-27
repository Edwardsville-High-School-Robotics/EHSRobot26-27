package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.ivy.groups.Groups.sequential;

import static java.util.concurrent.locks.LockSupport.park;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.systems.Feeder;
import org.firstinspires.ftc.teamcode.systems.Shooter;

public class RedFieldAuto {

    public Command autoRoutine(Follower follower, Shooter shooter, Feeder feeder) {
        Command shootCommand = Command.build().setStart(shooter::Shoot).setDone(()->{return !shooter.IsShooting();});
        Command startFeeding = Command.build().setStart(feeder::StartFeeding).setDone(()->{return true;});
        Command stopFeeding = Command.build().setStart(feeder::StopFeeding).setDone(()->{return true;});

        return sequential(

                // shoot
                shootCommand
        );
    }
}
