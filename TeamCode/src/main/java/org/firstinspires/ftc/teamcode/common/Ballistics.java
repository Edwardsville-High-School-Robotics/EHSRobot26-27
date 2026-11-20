package org.firstinspires.ftc.teamcode.common;

import static java.lang.Math.tan;

import org.firstinspires.ftc.teamcode.systems.Shooter;

public class Ballistics {
    static final double STARTING_FIRING_SPEED = 3.0;
    public static double SPIN_K_POLLEN = 2;   // tune: start at 0, raise until long shots stop overshooting
    public static double SPIN_K_NECTAR = 1.7;   // tune: start at 0, raise until long shots stop overshooting

    public static double GetFiringAngle(Coordinate3D goalPos, double roboHeading, double firingSpeed) {

        Coordinate3D relativePos = Matrix.RotateCoord(goalPos, 0, roboHeading, 0);

        relativePos.x += Shooter.LL_OFFSET.x;
        relativePos.y += Shooter.LL_OFFSET.y + Shooter.getYOffset();
        relativePos.z += Shooter.LL_OFFSET.z + Shooter.getZOffset();

        double df = 9.8;
        double discriminant = Math.pow(firingSpeed, 4) - 19.6 * Math.pow(firingSpeed, 2) * relativePos.y
                - 96.04 * Math.pow(relativePos.z, 2);
        double answer = (Math.pow(firingSpeed, 2) - Math.sqrt(discriminant)) / (9.8 * relativePos.z);
        return Math.atan(answer);
    }

    public static double GetLaunchSpeed(Coordinate3D goalPos, double roboHeading, double firingAngle, double spin_k) {
        Coordinate3D rel = Matrix.RotateCoord(goalPos, 0, roboHeading, 0);
        rel.x += Shooter.LL_OFFSET.x;
        rel.y += Shooter.LL_OFFSET.y + Shooter.getYOffset();
        rel.z += Shooter.LL_OFFSET.z + Shooter.getZOffset();

        double A = Math.toRadians(firingAngle);
        double c = Math.cos(A);
        double d = Math.hypot(rel.x, rel.z);
        double h = rel.y;
        double denom = 2 * c * c * (d * Math.tan(A) - h) + spin_k * c * d * d;
        if (denom <= 0) return Double.NaN;
        return Math.sqrt(9.8 * d * d / denom);
    }

    public static double GetFiringAngleInch(Coordinate3D goalPos, double roboHeading, double firingSpeed) {
        goalPos.x /= 39.3700787402;
        goalPos.y /= 39.3700787402;
        goalPos.z /= 39.3700787402;
        return GetFiringAngle(goalPos, roboHeading, firingSpeed);
    }

    // firing speed, firing angle
    public static FireInfo GetFireInfo(Coordinate3D goalPos, double roboHeading, double minimumRange) {
        FireInfo fireInfo = new FireInfo();
        fireInfo.launchAngle = 0;
        fireInfo.speed = 0;

        // code that does stuff
        double CurrentFiringSpeed = STARTING_FIRING_SPEED;
        double angle = GetFiringAngleInch(goalPos, roboHeading, STARTING_FIRING_SPEED);
        if (minimumRange > angle) {
            while (minimumRange > angle) {
                CurrentFiringSpeed /= 2;
                angle = GetFiringAngleInch(goalPos, roboHeading, CurrentFiringSpeed);
            }

        }
        fireInfo.speed = CurrentFiringSpeed;
        fireInfo.launchAngle = angle;

        return fireInfo;

    }

    public static double[] GetVelocityOffset(double heightM, double velocityX, double velocityZ, double vy) {
        double disc = vy * vy - 19.6 * heightM;
        if (disc < 0) return null;                  // can't reach that height
        double time = (vy + Math.sqrt(disc)) / 9.8; // descending crossing
        return new double[]{ -time * velocityX, -time * velocityZ };
    }

    public static FireInfo GetFireInfoVelocity(Coordinate3D goalPos, Coordinate3D velocity, double roboHeading, double firingAngle, double spin_k) {
        double speed = GetLaunchSpeed(goalPos, roboHeading, firingAngle, spin_k) * Shooter.FIRE_MOTOR_SPEED_OFFSET;
        if (Double.isNaN(speed)) return null;

        double vy = Math.sin(Math.toRadians(firingAngle)) * speed;
        double heightM = goalPos.y + Shooter.LL_OFFSET.y + Shooter.getYOffset(); // same height GetLaunchSpeed used
        double[] off = GetVelocityOffset(heightM, velocity.x, velocity.z, vy);
        if (off == null) return null;

        FireInfo info = new FireInfo();
        info.launchAngle = firingAngle;
        info.speed = speed;
        info.pointToLookAt = new Coordinate3D(goalPos.x + off[0], goalPos.y, goalPos.z + off[1]);
        return info;
    }

    public static FireInfo GetFireInfoWithVelocityInch(Coordinate3D goalPos, Coordinate3D velocity, double roboHeading, double firingAngle, double spin_k) {
        final double IN_PER_M = 39.3700787402;
        Coordinate3D g = new Coordinate3D(goalPos.x / IN_PER_M, -goalPos.y / IN_PER_M, goalPos.z / IN_PER_M);
        Coordinate3D v = new Coordinate3D(velocity.x / IN_PER_M, velocity.y / IN_PER_M, velocity.z / IN_PER_M);
        return GetFireInfoVelocity(g, v, roboHeading, firingAngle, spin_k);
    }

    public static FireInfo GetFireInfoTurretInch(Coordinate3D goalPos, Coordinate3D velocity, double roboHeading, double firingAngle, double spin_k)
    {
        final double IN_PER_M = 39.3700787402;
        Coordinate3D g = new Coordinate3D(goalPos.x / IN_PER_M, -goalPos.y / IN_PER_M, goalPos.z / IN_PER_M);
        Coordinate3D v = new Coordinate3D(velocity.x / IN_PER_M, velocity.y / IN_PER_M, velocity.z / IN_PER_M);
        return null;//GetFireInfo(g, roboHeading, firingAngle, spin_k);
    }

}