package frc.robot.components;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Shooter {
    private TalonFX shooterMotor;
    private TalonFX shooterFollower;
    private Boolean shooterRunning = false;

    public Shooter(TalonFX shooterMotor, TalonFX shooterFollower){
        this.shooterMotor = shooterMotor;
        this.shooterFollower = shooterFollower;
        this.shooterFollower.follow(shooterMotor);
        this.shooterFollower.setInverted(true);
    }

    public void shooterOn(){
        this.shooterMotor.set(ControlMode.PercentOutput, .5);
        shooterRunning = true;
    }

    public void shooterOff(){
        this.shooterMotor.set(ControlMode.PercentOutput, 0);
        shooterRunning = false;
    }

    public Boolean isShooterRunning(){
        return shooterRunning;
    }
}