// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.MotorSafety;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.math.trajectory.TrajectoryUtil;

import frc.robot.components.Drivetrain;
import frc.robot.components.OI;
import frc.robot.components.OI.DriveMode;
import frc.robot.common.AutoCommand;
import frc.robot.common.TrajectoryImporter;

import frc.robot.components.Intake;
import frc.robot.components.Conveyor;
import frc.robot.components.Shooter;
import frc.robot.components.Climber;
import frc.robot.components.Limelight;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.I2C.Port;

import java.lang.Math;

import javax.print.attribute.standard.PrinterIsAcceptingJobs;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drivetrain driveTrain;
  private AHRS navX;
  private Intake intake;
  private Shooter shooter;
  private Conveyor conveyor;
  private Climber climber;
  private Limelight limelight;
  private Timer timer = new Timer();
  private double startTime;
  
  private OI input;
  private static final double cpr = 6; // am-3132
  private static final double wheelDiameter = 6; // 6 inch wheel

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    m_chooser.addOption("High Auto", "High Auto");
    SmartDashboard.putData("Auto choices", m_chooser);
    CameraServer.startAutomaticCapture();

    Joystick drive = new Joystick(0);
    Joystick operator = new Joystick(1);
    input = new OI(drive, operator);
    
    //NavX
    navX = new AHRS(Port.kMXP);
    navX.calibrate();
    
    //Drivetrain
    CANSparkMax leftFront = new CANSparkMax(1,MotorType.kBrushless);  
    CANSparkMax leftRear = new CANSparkMax(2, MotorType.kBrushless);
    CANSparkMax rightFront = new CANSparkMax(3, MotorType.kBrushless); 
    CANSparkMax rightRear = new CANSparkMax(4, MotorType.kBrushless);
    Encoder leftEncoder = new Encoder(0,1,false, EncodingType.k4X);
    Encoder rightEncoder = new Encoder(2,3, false,EncodingType.k4X);
    leftEncoder.setDistancePerPulse(Math.PI * wheelDiameter / cpr);
    rightEncoder.setDistancePerPulse(Math.PI * wheelDiameter / cpr);
    leftRear.follow(leftFront);
    rightFront.follow(rightRear);
    rightFront.setInverted(true);
    rightRear.setInverted(true);
    this.driveTrain = new Drivetrain(leftFront, leftRear, rightFront, rightRear, navX);

    //Intake
    CANSparkMax intakeMotor = new CANSparkMax(5, MotorType.kBrushless);
    this.intake = new Intake(intakeMotor);

    //Conveyor
    CANSparkMax conveyorMotor = new CANSparkMax(6, MotorType.kBrushless);
    CANSparkMax indexMotor = new CANSparkMax(7, MotorType.kBrushless);
    this.conveyor = new Conveyor(conveyorMotor, indexMotor);

    //Shooter
    TalonFX shooterMotor = new TalonFX(8);
    TalonFX shooterFollower = new TalonFX(9);
    this.shooter = new Shooter(shooterMotor, shooterFollower);

    //Climber
    CANSparkMax climberMotor = new CANSparkMax(10, MotorType.kBrushless);
    this.climber = new Climber(climberMotor);

    //Limelight
    limelight = new Limelight();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    startTime = Timer.getFPGATimestamp();
    timer.reset();
    timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case "High Auto":
      double time = Timer.getFPGATimestamp();  
      if(time - startTime < 3){
        this.shooter.shooterOn();
      }else{
        this.shooter.shooterOff();
      }
      if(time - startTime < 3 && time - startTime > 2.5){
        this.conveyor.revIndex();
      }else{
        this.conveyor.offIndex();
      }
      if(time - startTime < 7 && time - startTime > 3.1){
        this.driveTrain.tankDriveCustom(-.7, .7);
      }else{
        this.driveTrain.tankDriveCustom(0, 0);
      }
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    double driveY = -input.driver.getRawAxis(1);
    double zRotation = input.driver.getRawAxis(2);
    double rightDriveY = input.driver.getRawAxis(3);
    SmartDashboard.putString("Drivemode", input.getDriveMode().name()); // What is the current driving mode 
    driveTrain.update();
    // Driving Modes logic
    if (input.getDriveMode() == DriveMode.SPEED) {
      driveTrain.drive.arcadeDrive(driveY * .75, zRotation * .75);
      // Speed
    } else if (input.getDriveMode() == DriveMode.PRECISION) {
      // Double check that they are the right controls
      // Precision
      driveTrain.drive.tankDrive(driveY * .70, -rightDriveY * .70);
      // make turning senetive but forward about .50
    } else {
      driveTrain.drive.arcadeDrive(driveY*.70, zRotation*.70);
    }
    
    
    
    // Driving modes
    if (input.driver.getRawButton(1)) {
      // Set Speed Mode
      input.setDriveMode(DriveMode.SPEED);      
    } else if (input.driver.getRawButton(2)) {
      // Precision
      input.setDriveMode(DriveMode.PRECISION);
    } else if (input.driver.getRawButton(3)) {
      // Default
      input.setDriveMode(DriveMode.DEFAULT);
    }
    
    //Conveyor
    if(input.operator.getRawButton(5)){
      this.conveyor.on();
    }else if(input.operator.getRawButton(6)){
      this.conveyor.reverse();
    }else{
      this.conveyor.off();
    }if(input.operator.getRawButton(2)){
      this.conveyor.onIndex();
    }else if(input.operator.getRawButton(8)){
      this.conveyor.revIndex();
    }else{
      this.conveyor.offIndex();
    }

    //Intake
    if(input.operator.getRawButton(3)){
      this.intake.on();
    }else if(input.operator.getRawButton(4)){
      this.intake.reverse();
    }else{
      this.intake.off();
    }

    //Climber
    if(input.operator.getRawButton(11)){
      this.climber.climberUp();
    }else if(input.operator.getRawButton(12)){
      this.climber.climberDown();
    }else{
      this.climber.climberOff();
    }

    //Shooter
    SmartDashboard.putBoolean("Valid_Target", limelight.hasValidTarget());
    limelight.LedOn();
    if(input.operator.getRawButton(1)){
      this.shooter.shooterOn();
    }else if(input.operator.getRawButton(7)){
      this.shooter.shooterLow();
    }else{
      this.shooter.shooterOff();
    }
    
    
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
