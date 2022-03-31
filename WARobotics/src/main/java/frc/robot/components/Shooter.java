package frc.robot.components;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.common.PID;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;

import frc.robot.components.Limelight;


public class Shooter {
    private TalonFX shooterMotor;
    private TalonFX shooterFollower;
    private Boolean shooterRunning = false;
    private Encoder shooterEncoder;

    private double wheelRadius;
    private double wheelConversionFactor;
    
    private double kF = 0;
    private double kP = .30;
    private double kI = 0.001;
    private double kD = 0.09;
    private PIDController pidController = new PIDController(kP, kI, kD);

    private Shuffleboard shuffleboard;
    

    public Shooter(TalonFX shooterMotor, TalonFX shooterFollower){
        this.shooterMotor = shooterMotor;
        this.shooterFollower = shooterFollower;
        this.shooterFollower.follow(shooterMotor);
        this.shooterFollower.setInverted(true);
       

        shooterMotor.configFactoryDefault(); //Set the config of the robot
        pidController.setPID(kP, kI, kD);
    
    }

    public void shooterOn(){
        this.shooterMotor.set(ControlMode.PercentOutput, .6);
        
        shooterRunning = true;
    }

    public void shooterLow(){
        this.shooterMotor.set(ControlMode.PercentOutput, .25);
        shooterRunning = true;
    }

    public void shooterOff(){
        this.shooterMotor.set(ControlMode.PercentOutput, 0);
        shooterRunning = false;
    }

    public Boolean isShooterRunning(){
        return shooterRunning;
    }

    public double getTargetVelocity(){
        double shooterSpeedMain = (double)(shooterMotor.getSelectedSensorVelocity()/3057); //Finds the optimal velocity for the shooter motor
        double temp = shooterSpeedMain;
        return temp;
    }


    public double getVelocity(){
        double velocity = shooterMotor.getSelectedSensorVelocity();
        return (velocity);
        
    }   
}