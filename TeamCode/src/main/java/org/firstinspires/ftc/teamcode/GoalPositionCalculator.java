public class GoalPositionCalculator {
    

public static Coordinate3D GetGoalPos(
            double angleX,
            double angleY,
            double distance) {

        double x = distance
                * Math.cos(Math.toRadians(angleY))
                * Math.cos(Math.toRadians(angleX));

        double y = distance
                * Math.cos(Math.toRadians(angleY))
                * Math.sin(Math.toRadians(angleX));

        double z = distance
                * Math.sin(Math.toRadians(angleY));

        return new Coordinate3D(x, y, z);
    }
}