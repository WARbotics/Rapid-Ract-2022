package frc.robot.components;

import com.revrobotics.CANSparkMax;

public class Climber {
    private CANSparkMax climberMotor;
    private boolean isClimberRunning = false;
    public Climber(CANSparkMax climberMotor){
        this.climberMotor = climberMotor;
    }

    public void climberUp(){
        this.climberMotor.set(1);
        this.isClimberRunning = true;
    }
    public void climberDown(){
        this.climberMotor.set(-1);
        this.isClimberRunning = true;
    }
    public void climberOff(){
        this.climberMotor.set(0);
        this.isClimberRunning = false;
    }
    public boolean climberRunning(){
        return this.isClimberRunning;
    }

}
