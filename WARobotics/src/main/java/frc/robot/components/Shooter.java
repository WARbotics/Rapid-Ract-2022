package frc.robot.components;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Shooter {
    private TalonFX shooter;
    private TalonFX shooterFollower;
    private Boolean shooterRunning = false;

    public Shooter(TalonFX shooter, TalonFX shooterFollower){
        this.shooter = shooter;
        this.shooterFollower = shooterFollower;
        this.shooterFollower.follow(shooter);
        this.shooterFollower.setInverted(true);
    }

    public void shooterOn(){
        this.shooter.set(ControlMode.PercentOutput, .5);
        shooterRunning = true;
    }

    public void shooterOff(){
        this.shooter.set(ControlMode.PercentOutput, 0);
        shooterRunning = false;
    }

    public Boolean isShooterRunning(){
        return shooterRunning;
    }
}