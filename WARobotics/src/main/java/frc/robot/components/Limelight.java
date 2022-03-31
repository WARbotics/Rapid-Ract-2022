package frc.robot.components;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.lang.Math;

public class Limelight {
    private NetworkTable table;
    private boolean isLedOn = false;
    public Limelight(){ 
        this.table = NetworkTableInstance.getDefault().getTable("limelight"); //Sets the limelight status
    }

    public void LedOn(){
        this.table.getEntry("ledMode").setNumber(3);
        isLedOn = true;
    }

    public void LedOff(){
        this.table.getEntry("ledMode").setNumber(1);
        isLedOn = false;
    }
    public boolean getLedStatus(){
        return this.isLedOn;
    }

    // Get's the vertical value
    public double getY(){
        NetworkTableEntry ty = this.table.getEntry("ty");
        return ty.getDouble(0.0);
    }

    // Gets the horizontal value
    public double getX(){
        NetworkTableEntry tx = this.table.getEntry("tx");
        return tx.getDouble(0.0);

    }

    // Gets the target area
    public double getArea(){
        NetworkTableEntry ta = this.table.getEntry("ta");
        return ta.getDouble(0.0);

    }

    
    // Checks if there is a valid target
    public boolean hasValidTarget(){
        NetworkTableEntry tv = this.table.getEntry("tv");
        double _tv =  tv.getDouble(0.0);
        if(_tv >= 1.0){
            return true; 
        }
        return false;
    }

    public double getDistance(){
        //Finds the distance from the limelight to the target area
          double h1 = DriveConstants.robotShooterHeight; //Hight of the limelight mounted on the robot (CHANGE LATER)
          double h2 = 1.90; //Hight of the goal
          double a1 = DriveConstants.robotShooterAngle; //Fixed angle of shooter on robot (CHANGE LATER)
          double a2 = this.getY(); 
          
          double distance = (h2-h1)/Math.tan(Math.toRadians(a1)-Math.toRadians(a2));
          if(distance < 0){
              return 0;
          }else{
              return distance;
          }
        }
        
    
      

      
    
}
