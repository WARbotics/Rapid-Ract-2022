package frc.robot.common;
//find the optimal rate of change at a given instance in order to achieve the desired value smoothly
public class PID {
    private double P, I, D;
    private double setPoint = 0;
    private double integral, previous_error;
    private double rate;
    private double error = 0;

    public PID(double P, double I, double D) {
        this.P = P;
        this.I = I;
        this.D = D;
    }

    //setter method to set desired value
    public void setPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    //according to the actual value passed in as a perimeter, the optimal rate of change is calculated
    public void setActual(double actual) {
        this.error = (setPoint - actual);
        this.integral += (error * I);
        double derivative = (error - this.previous_error) / .02;
        this.rate = (P * error) + (I * this.integral) + (D * derivative);
    }

    //getter method for error
    public double getError(){
        return this.error;
    }

    //getter method for optimal rate
    public double getRate() {
        return this.rate;
    }
}