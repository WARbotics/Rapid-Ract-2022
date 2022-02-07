package frc.robot.components;

import com.revrobotics.CANSparkMax;

public class Conveyor {
    private CANSparkMax conveyorMotor;
    private CANSparkMax index;
    private Boolean conveyorRunning = false;
    private Boolean indexRunning = false;


    public Conveyor(CANSparkMax conveyorMotor, CANSparkMax indexMotor){
        this.conveyorMotor = conveyorMotor;
        this.index = indexMotor;
    }

    public void on(){
        this.conveyorMotor.set(.5);
        conveyorRunning = true;
    }


    public void off(){
        this.conveyorMotor.set(0);
        conveyorRunning  = false;
    }

    public void reverse(){
        this.conveyorMotor.set(-.5);
        conveyorRunning = true;
    }

    public void onIndex(){
        this.index.set(.75);
        indexRunning = true;
    }
    public void offIndex(){
        this.index.set(0);
        indexRunning = false;
    }
    public void revIndex(){
        this.index.set(-.75);
        indexRunning = true;
    }

    
    public boolean isConveyorRunning (){
        return conveyorRunning;
    }

    public boolean isIndexRunning (){
        return indexRunning;
    }
}
