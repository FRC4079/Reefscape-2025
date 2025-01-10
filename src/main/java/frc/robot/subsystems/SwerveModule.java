package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TorqueCurrentConfigs;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utils.PID;
import frc.robot.utils.RobotParameters.*;
import frc.robot.utils.RobotParameters.SwerveParameters.*;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

/** Represents a swerve module used in a swerve drive system. */
public class SwerveModule {
  private final TalonFX driveMotor;
  private final CANcoder canCoder;
  private final TalonFX steerMotor;
  private final PositionTorqueCurrentFOC positionSetter;
  private final VelocityTorqueCurrentFOC velocitySetter;
  private final SwerveModulePosition swerveModulePosition;
  private SwerveModuleState state;
  private double driveVelocity;
  private double drivePosition;
  private double steerPosition;
  private double steerVelocity;
  private final TalonFXConfiguration driveConfigs;
  private final TalonFXConfiguration steerConfigs;
  private final TorqueCurrentConfigs driveTorqueConfigs;

  private LoggedNetworkNumber driveP;
  private LoggedNetworkNumber driveI;
  private LoggedNetworkNumber driveD;
  private LoggedNetworkNumber driveV;

  private LoggedNetworkNumber steerP;
  private LoggedNetworkNumber steerI;
  private LoggedNetworkNumber steerD;
  private LoggedNetworkNumber steerV;

  private Alert driveDisconnectedAlert;
  private Alert turnDisconnectedAlert;
  private Alert canCoderDisconnectedAlert;

  /**
   * Constructs a new SwerveModule.
   *
   * @param driveId The ID of the drive motor.
   * @param steerId The ID of the steer motor.
   * @param canCoderID The ID of the CANcoder.
   * @param canCoderDriveStraightSteerSetPoint The set point for the CANcoder drive straight steer.
   */
  public SwerveModule(
      int driveId, int steerId, int canCoderID, double canCoderDriveStraightSteerSetPoint) {
    driveMotor = new TalonFX(driveId);
    canCoder = new CANcoder(canCoderID);
    steerMotor = new TalonFX(steerId);
    positionSetter = new PositionTorqueCurrentFOC(0.0).withSlot(0);
    velocitySetter = new VelocityTorqueCurrentFOC(0.0);
    swerveModulePosition = new SwerveModulePosition();
    state = new SwerveModuleState(0.0, Rotation2d.fromDegrees(0.0));

    driveConfigs = new TalonFXConfiguration();

    // Set the PID values for the drive motor
    driveConfigs.Slot0.kP = PIDParameters.DRIVE_PID_AUTO.getP();
    driveConfigs.Slot0.kI = PIDParameters.DRIVE_PID_AUTO.getI();
    driveConfigs.Slot0.kD = PIDParameters.DRIVE_PID_AUTO.getD();
    driveConfigs.Slot0.kV = PIDParameters.DRIVE_PID_V_AUTO;

    // Sets the brake mode, invered, and current limits for the drive motor
    driveConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    driveConfigs.MotorOutput.Inverted = SwerveParameters.Thresholds.DRIVE_MOTOR_INVERTED;
    driveConfigs.CurrentLimits.SupplyCurrentLimit = MotorParameters.DRIVE_SUPPLY_LIMIT;
    driveConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    driveConfigs.CurrentLimits.StatorCurrentLimit = MotorParameters.DRIVE_STATOR_LIMIT;
    driveConfigs.CurrentLimits.StatorCurrentLimitEnable = true;

    steerConfigs = new TalonFXConfiguration();

    // Set the PID values for the steer motor
    steerConfigs.Slot0.kP = PIDParameters.STEER_PID_AUTO.getP();
    steerConfigs.Slot0.kI = PIDParameters.STEER_PID_AUTO.getI();
    steerConfigs.Slot0.kD = PIDParameters.STEER_PID_AUTO.getD();
    steerConfigs.Slot0.kV = 0.0;

    // Sets the brake mode, inverted, and current limits for the steer motor
    steerConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    steerConfigs.MotorOutput.Inverted = SwerveParameters.Thresholds.STEER_MOTOR_INVERTED;
    steerConfigs.Feedback.FeedbackRemoteSensorID = canCoderID;
    steerConfigs.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
    steerConfigs.Feedback.RotorToSensorRatio = MotorParameters.STEER_MOTOR_GEAR_RATIO;
    steerConfigs.CurrentLimits.SupplyCurrentLimit = MotorParameters.STEER_SUPPLY_LIMIT;
    steerConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;

    driveTorqueConfigs = new TorqueCurrentConfigs();

    CANcoderConfiguration canCoderConfiguration = new CANcoderConfiguration();

    /*
     * Sets the CANCoder direction, absolute sensor range, and magnet offset for the CANCoder Make
     * sure the magnet offset is ACCURATE and based on when the wheel is straight!
     */
    // canCoderConfiguration.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5; TODO: Change
    // default value
    canCoderConfiguration.MagnetSensor.SensorDirection =
        SensorDirectionValue.CounterClockwise_Positive;
    canCoderConfiguration.MagnetSensor.MagnetOffset =
        SwerveParameters.Thresholds.ENCODER_OFFSET + canCoderDriveStraightSteerSetPoint;

    driveMotor.getConfigurator().apply(driveConfigs);
    steerMotor.getConfigurator().apply(steerConfigs);
    canCoder.getConfigurator().apply(canCoderConfiguration);

    driveVelocity = driveMotor.getVelocity().getValueAsDouble();
    drivePosition = driveMotor.getPosition().getValueAsDouble();
    steerVelocity = steerMotor.getVelocity().getValueAsDouble();
    steerPosition = steerMotor.getPosition().getValueAsDouble();

    initializeLoggedNetworkPID();
    intializeAlarms(driveId, steerId, canCoderID);
  }

  /**
   * Gets the current position of the swerve module.
   *
   * @return The current position of the swerve module.
   */
  public SwerveModulePosition getPosition() {
    driveVelocity = driveMotor.getVelocity().getValueAsDouble();
    drivePosition = driveMotor.getPosition().getValueAsDouble();
    steerVelocity = steerMotor.getVelocity().getValueAsDouble();
    steerPosition = steerMotor.getPosition().getValueAsDouble();

    swerveModulePosition.angle =
        Rotation2d.fromDegrees(
            ((360 * canCoder.getAbsolutePosition().getValueAsDouble()) % 360 + 360) % 360);
    swerveModulePosition.distanceMeters =
        (drivePosition / MotorParameters.DRIVE_MOTOR_GEAR_RATIO * MotorParameters.METERS_PER_REV);

    return swerveModulePosition;
  }

  /**
   * Gets the current state of the swerve module.
   *
   * @return The current state of the swerve module, including the angle and speed.
   */
  public SwerveModuleState getState() {
    state.angle = Rotation2d.fromRotations(steerMotor.getPosition().getValueAsDouble());
    state.speedMetersPerSecond =
        (driveMotor.getRotorVelocity().getValueAsDouble()
            / MotorParameters.DRIVE_MOTOR_GEAR_RATIO
            * MotorParameters.METERS_PER_REV);
    return state;
  }

  /**
   * Sets the state of the swerve module.
   *
   * @param value The desired state of the swerve module.
   */
  public void setState(SwerveModuleState value) {
    // Get the current position of the swerve module
    SwerveModulePosition newPosition = getPosition();

    // Optimize the state based on the current position
    state.optimize(newPosition.angle);

    // Set the angle for the steer motor
    double angleToSet = state.angle.getRotations();
    steerMotor.setControl(positionSetter.withPosition(angleToSet));

    // Set the velocity for the drive motor
    double velocityToSet =
        (state.speedMetersPerSecond
            * (MotorParameters.DRIVE_MOTOR_GEAR_RATIO / MotorParameters.METERS_PER_REV));
    driveMotor.setControl(velocitySetter.withVelocity(velocityToSet));

    // Log the actual and set values for debugging
    // dash(
    //   pairOf("drive actual speed " + canCoder.getDeviceID(),
    // driveMotor.getVelocity().getValueAsDouble()),
    //   pairOf("drive set speed " + canCoder.getDeviceID(), velocityToSet),
    //   pairOf("steer actual angle " + canCoder.getDeviceID(),
    // steerMotor.getRotorPosition().getValueAsDouble()),
    //   pairOf("steer set angle " + canCoder.getDeviceID(), angleToSet),
    //   pairOf("desired state after optimize " + canCoder.getDeviceID(),
    // state.angle.getRotations())
    // );

    // Update the state
    state = value;
  }

  /** Stops the swerve module motors. */
  public void stop() {
    steerMotor.stopMotor();
    driveMotor.stopMotor();
  }

  /**
   * Sets the drive PID values.
   *
   * @param pid The PID object containing the PID values.
   * @param velocity The velocity value.
   */
  public void setDrivePID(PID pid, double velocity) {
    driveConfigs.Slot0.kP = pid.getP();
    driveConfigs.Slot0.kI = pid.getI();
    driveConfigs.Slot0.kD = pid.getD();
    driveConfigs.Slot0.kV = velocity;
    driveMotor.getConfigurator().apply(driveConfigs);
  }

  /**
   * Sets the steer PID values.
   *
   * @param pid The PID object containing the PID values.
   * @param velocity The velocity value.
   */
  public void setSteerPID(PID pid, double velocity) {
    steerConfigs.Slot0.kP = pid.getP();
    steerConfigs.Slot0.kI = pid.getI();
    steerConfigs.Slot0.kD = pid.getD();
    steerConfigs.Slot0.kV = velocity;
    steerMotor.getConfigurator().apply(steerConfigs);
  }

  public void applyTelePIDValues() {
    driveConfigs.Slot0.kP = driveP.get();
    driveConfigs.Slot0.kI = driveI.get();
    driveConfigs.Slot0.kD = driveD.get();

    steerConfigs.Slot0.kP = steerP.get();
    steerConfigs.Slot0.kI = steerI.get();
    steerConfigs.Slot0.kD = steerD.get();

    driveMotor.getConfigurator().apply(driveConfigs);
    steerMotor.getConfigurator().apply(steerConfigs);
  }

  /** Sets the PID values for teleoperation mode. */
  public void setTelePID() {
    setDrivePID(PIDParameters.DRIVE_PID_TELE, PIDParameters.DRIVE_PID_V_TELE);
    setSteerPID(PIDParameters.STEER_PID_TELE, 0.0);
  }

  /** Sets the PID values for autonomous mode. */
  public void setAutoPID() {
    setDrivePID(PIDParameters.DRIVE_PID_AUTO, PIDParameters.DRIVE_PID_V_AUTO);
  }

  /** Resets the drive motor position to zero. */
  public void resetDrivePosition() {
    driveMotor.setPosition(0.0);
  }

  public void initializeLoggedNetworkPID() {
    driveP = new LoggedNetworkNumber("Drive P", driveConfigs.Slot0.kP);
    driveI = new LoggedNetworkNumber("Drive I", driveConfigs.Slot0.kI);
    driveD = new LoggedNetworkNumber("Drive D", driveConfigs.Slot0.kD);
    driveV = new LoggedNetworkNumber("Drive V", driveConfigs.Slot0.kV);

    steerP = new LoggedNetworkNumber("Steer P", steerConfigs.Slot0.kP);
    steerI = new LoggedNetworkNumber("Steer I", steerConfigs.Slot0.kI);
    steerD = new LoggedNetworkNumber("Steer D", steerConfigs.Slot0.kD);
    steerV = new LoggedNetworkNumber("Steer V", steerConfigs.Slot0.kV);
  }

  public void intializeAlarms(int driveID, int steerID, int canCoderID) {
    driveDisconnectedAlert =
        new Alert("Disconnected drive motor " + Integer.toString(driveID) + ".", AlertType.kError);
    turnDisconnectedAlert =
        new Alert("Disconnected turn motor " + Integer.toString(steerID) + ".", AlertType.kError);
    canCoderDisconnectedAlert =
        new Alert("Disconnected CANCoder " + Integer.toString(canCoderID) + ".", AlertType.kError);

    driveDisconnectedAlert.set(!driveMotor.isConnected());
    turnDisconnectedAlert.set(!steerMotor.isConnected());
    canCoderDisconnectedAlert.set(!canCoder.isConnected());
  }

  public void updateTelePID() {
    PIDParameters.DRIVE_PID_TELE.setP(driveP.get());
    PIDParameters.DRIVE_PID_TELE.setI(driveI.get());
    PIDParameters.DRIVE_PID_TELE.setD(driveD.get());

    PIDParameters.STEER_PID_TELE.setP(steerP.get());
    PIDParameters.STEER_PID_TELE.setI(steerI.get());
    PIDParameters.STEER_PID_TELE.setD(steerD.get());

    applyTelePIDValues();
  }
}
