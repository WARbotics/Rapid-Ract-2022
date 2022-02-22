package frc.robot.components;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

public class DriveConstants {


    // The Robot Characterization Toolsuite provides a convenient tool for obtaining these
    // values for your robot.
    public static final double ksVolts = .115; // .0967
    public static final double kvVoltSecondsPerMeter = 2.76; // 3.15
    public static final double kaVoltSecondsSquaredPerMeter = 0.467; // .426

    // Example value only - as above, this must be tuned for your drive!
    public static final double kPDriveVel = .00291;
    	

    public static final double kTrackwidthMeters = .60; // 4.5 <- this could possibly be wrong 
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);

        	

    public static final double kMaxSpeedMetersPerSecond = 3.25;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1;

        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;


    // Robot 
    public static final double robotShooterHeight = 0.46;
    public static final double robotShooterAngle = 26;

}
