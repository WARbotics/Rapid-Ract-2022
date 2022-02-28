package frc.robot.components;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

public class Shooter {
    private TalonFX shooterMotor;
    private TalonFX shooterFollower;
    private Boolean shooterRunning = false;

    private double kF = 0;
    private double kP = 0;
    private double kI = 0;
    private double kD = 0;

    public Shooter(TalonFX shooterMotor, TalonFX shooterFollower){
        this.shooterMotor = shooterMotor;
        this.shooterFollower = shooterFollower;
        this.shooterFollower.follow(shooterMotor);
        this.shooterFollower.setInverted(true);

        shooterMotor.configFactoryDefault(); //Set the config of the robot
       

        shooterMotor.config_kF(0, kF, 30); // Set the confif for the PID
        shooterMotor.config_kP(0, kP, 30);
        shooterMotor.config_kI(0, kI, 30);
        shooterMotor.config_kD(0, kD, 30);
    }

    public void shooterOn(){
        this.shooterMotor.set(ControlMode.PercentOutput, .7);
        shooterRunning = true;
    }

    public void shooterOff(){
        this.shooterMotor.set(ControlMode.PercentOutput, 0);
        shooterRunning = false;
    }

    public Boolean isShooterRunning(){
        return shooterRunning;
    }

    public void pidUpdate(){
        ShuffleboardTab tab = Shuffleboard.getTab("Shooter");
        NetworkTableEntry shooterEnable = tab.add("Shooter Enable", false).getEntry();
    
    }
}