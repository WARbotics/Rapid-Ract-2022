package frc.robot.common;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
//This object prevents the accidental touch to move the robot parts
public class ButtonDebouncer {
    Joystick joystick;
    int buttonNum;
    double latest;
    double debouncePeriod;
    //intialize the variables
    public ButtonDebouncer(Joystick joystick, int buttonNum, double debouncePeriod) {
        this.joystick = joystick;
        this.buttonNum = buttonNum;
        this.latest = 0;
        this.debouncePeriod = debouncePeriod;
    }
    //setter method for the period during which the actions on buttons are considered illegal
    public void setDebouncePeriod(double debouncePeriod) {
        this.debouncePeriod = debouncePeriod;
    }
    /*if the time from the latest action on button to present is longer than deboucePeriod
    *update the latest time of action to now and return true
    *elsewise, the action is illegal
    */
    public boolean isReady() {
        double now = Timer.getFPGATimestamp();
        if (joystick.getRawButton(buttonNum)) {
            if ((now - latest) > debouncePeriod) {
                latest = now;
                return true;
            }
        }
        return false;

    }
}