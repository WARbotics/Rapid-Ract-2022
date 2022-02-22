package frc.robot.components;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax; 
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.common.PID;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
    private CANSparkMax leftFront; 
    private CANSparkMax leftRear; 


    
    private CANSparkMax rightFront; 
    private CANSparkMax rightRear; 

    private SpeedControllerGroup left, right;
    public EncoderGroup leftEncoder, rightEncoder; 
    public DifferentialDrive drive;

    private AHRS navx; 
    

    private double deadBand = 0.0;
    private PID PID = new PID(0.30, 0.00, 0.01);
    private double speed = 0;
    private double rotation = 0; 
    private DifferentialDriveOdometry odometry;
    private Field2d field = new Field2d();

/*
*This instantuates the leftFront and other motors as CANSparkMax
*/

    public Drivetrain(CANSparkMax leftFront,CANSparkMax leftRear,CANSparkMax rightFront,  CANSparkMax rightRear,AHRS navX) {
        
        // LEFT FRONT
        this.leftFront = leftFront;

        // LEFT REAR
        this.leftRear = leftRear;
    
        this.left = new SpeedControllerGroup(leftFront, leftRear);

        // RIGHT FRONT
        this.rightFront = rightFront;


        // RIGHT REAR
        this.rightRear = rightRear;
 
        this.right = new SpeedControllerGroup(this.rightFront, this.rightRear);
/*
*This groups each side encoder together
*/
        this.leftEncoder = new EncoderGroup(this.leftFront.getEncoder(), this.leftRear.getEncoder());
        this.rightEncoder = new EncoderGroup(this.rightFront.getEncoder(), this.rightRear.getEncoder());
        this.drive = new DifferentialDrive(left, right);

        resetEncoders();

        //Sensor 
        this.navx = navX; 
        this.navx.setAngleAdjustment(0);
        
        this.odometry = new DifferentialDriveOdometry(this.getRotation2d()); // Get the current angle and converts to rads 


    }
    /**
    * Sets the deadband for the drivetrain 
    *
    * @param deadBand the amount of deadband that will be appiled to the drivetrain 
    */
    public void setDeadBand(double deadBand) {
        this.deadBand = deadBand;
    }
    /**
    * Gets the speed for the drivetrain 
    *
    */
    public double getSpeed() {
        return speed;
    }
/*
*gets the rotation for the drivetrain
*/
    public double getRotation() {
        return rotation;
    }
/*
*drives the robot in a curve
*/
    public void curveDrive(double speed, double rotation, boolean isQuickTurn) {
        if (Math.abs(speed) <= this.deadBand) {
            speed = 0;
        }
        if (Math.abs(rotation) <= this.deadBand) {
            rotation = 0;
        }
        this.speed = speed;
        this.rotation = rotation;
        PID.setActual(this.speed);
        drive.curvatureDrive(this.speed, this.rotation, isQuickTurn);
    }
/*
*runs tank drive via volatge
*/
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        left.setVoltage(leftVolts * .60);
        right.setVoltage(-rightVolts * .60);
        drive.feed();
      }
/* 
*gets rotation
*/
    private Rotation2d getRotation2d(){
        return this.navx.getRotation2d();
    }

    public void resetEncoders(){
        this.rightEncoder.reset();
        this.leftEncoder.reset();
    }
   /*
   *gets the position
   */ 
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void resetOdomentry(Pose2d pose){
        this.resetEncoders();
        odometry.resetPosition(pose, this.getRotation2d());
    }
/*
*this gets average encoder distance
*/     

    public double getAverageEncoderDistance() {
        return (leftEncoder.getDistance() + -1*(rightEncoder.getDistance())) / 2.0; 
    }
    public void tankDriveCustom(double leftSpeed, double rightSpeed){
        left.set(leftSpeed * .60);
        right.set(-rightSpeed * .60);
        drive.feed();
    }
/*
*Sensor
*/
    public void zeroHeading() {
        this.navx.reset();
    }
    public double getHeading(){
        return this.getRotation2d().getDegrees();
    }
    public double getTurnRate() {
        return -(this.navx.getRate());
    }
/*
* this directly tells us the values SmartDashboard
*/
    public void update(){
        SmartDashboard.putNumber("Heading", this.getHeading());
        SmartDashboard.putNumber("Left Velocity", this.leftEncoder.getVelocity());
        SmartDashboard.putNumber("Right Velocity", this.rightEncoder.getVelocity());
        SmartDashboard.putNumber("Left Encoder Group Distance", this.leftEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder Group Distance", this.rightEncoder.getDistance());
        odometry.update(this.getRotation2d(), this.leftEncoder.getDistance(), -this.rightEncoder.getDistance());
        SmartDashboard.putNumber("x odometry", odometry.getPoseMeters().getX());
        SmartDashboard.putNumber("y odometry", odometry.getPoseMeters().getY());
        field.setRobotPose(odometry.getPoseMeters());
        SmartDashboard.putData("Field", field);
    }

}