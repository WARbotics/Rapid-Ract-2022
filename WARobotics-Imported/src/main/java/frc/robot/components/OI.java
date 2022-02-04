package frc.robot.components;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    public Joystick driver;
    public Joystick operator;
    private DriveMode mode = DriveMode.DEFAULT;

    public OI(Joystick driver, Joystick operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public enum DriveMode {
        DEFAULT, PRECISION, SPEED
    }

    public DriveMode getDriveMode() {
        return mode;
    }

    public void setDriveMode(DriveMode mode) {

        this.mode = mode;
    }

}
