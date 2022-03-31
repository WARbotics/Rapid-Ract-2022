package frc.robot.common;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.Filesystem;

public class TrajectoryImporter {
    public static Path getTrajectory(String trajectoryPath) throws IOException {
        return Filesystem.getDeployDirectory().toPath().resolve(trajectoryPath);
    }
    
}
