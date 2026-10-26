
import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class RedAudiencePath {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(58.1274, 4.1954, 90);
    private final Pose point1 = poseFactory.of(15.1981, 3.5593, 0.8489);
    private final Pose point2 = poseFactory.of(58.0315, 4.887, -178.2246);
    private final Pose point3 = poseFactory.of(7.8611, 100.513, -62.3162);

    public Path path1() {
        return Paths.line(start, point1).reverseTangent();
    }

    public Path path2() {
        return Paths.line(point1, point2).reverseTangent();
    }

    public Path path3() {
        return Paths.line(point2, point3).reverseTangent();
    }
}