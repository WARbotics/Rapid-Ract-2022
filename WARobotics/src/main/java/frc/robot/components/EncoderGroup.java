package frc.robot.components;

import com.revrobotics.RelativeEncoder;

/**
 * This sets up the built-in CAN Encoders conversions factors. The other methods get
 * the distance as well as the velocity using the predetermined conversion factors.
 */
public class EncoderGroup {
    public RelativeEncoder encoderLeader; 
    public RelativeEncoder encoderFollower;
    private double conversionFactor = 21.4523 ; // (One rotation in rotation values) 

    // 4096
    public EncoderGroup(RelativeEncoder encoderLeader, RelativeEncoder encoderFollower){
        this.encoderLeader = encoderLeader;
        this.encoderFollower = encoderFollower;
    }

    public void setConversionFactor(double factor){
        this.conversionFactor = factor;
    }

    public double getDistance(){
        return ((this.encoderFollower.getPosition() + this.encoderLeader.getPosition()) / this.conversionFactor)*0.478779;
    } 
    public double getVelocity(){
        return ((this.encoderLeader.getVelocity()+this.encoderFollower.getVelocity())/4096) * (1/10.75) * 0.478779;
    }
    
    public void reset(){
        this.encoderFollower.setPosition(0);
        this.encoderLeader.setPosition(0);
    }




}

