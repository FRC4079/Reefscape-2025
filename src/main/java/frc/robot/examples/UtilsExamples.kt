package frc.robot.examples

import io.github.frc4079.reefscape.utils.*

/**
 * Example showing how to use the multiplatform utils library in robot code.
 */
object UtilsExamples {
    
    /**
     * Example of using Pingu for PID configuration.
     */
    fun pidExample() {
        // Create a PID controller configuration
        val drivetrainPid = Pingu(
            p = 5.0,
            i = 0.0, 
            d = 0.0,
            v = 0.7,  // velocity feedforward
            s = 0.5   // static feedforward
        )
        
        // Use with WPILib PIDController
        val pidController = drivetrainPid.pidController
        
        // Check for feedforward values
        if (drivetrainPid.hasVelocityGain()) {
            println("Velocity FF: ${drivetrainPid.getVelocityGain()}")
        }
        
        // Create modified versions
        val tuned = drivetrainPid.withP(6.0).withI(0.1)
    }
    
    /**
     * Example of using MagicPingu for motion profiling.
     */
    fun motionProfileExample() {
        val constraints = MagicPingu(
            velocity = 5.0,     // m/s
            acceleration = 3.0, // m/s²
            jerk = 10.0         // m/s³
        )
        
        // Validate constraints are positive
        constraints.validate()
        
        // Create faster profile
        val fastConstraints = constraints.withVelocity(8.0)
    }
    
    /**
     * Example of using math utilities.
     */
    fun mathExample() {
        // Angle conversions
        val targetAngle = 45.0.degreesToRadians()
        val currentAngle = 50.0.degreesToRadians()
        
        // Calculate shortest angular distance  
        val error = MathUtils.angularDistance(currentAngle, targetAngle)
        
        // Apply deadband to joystick input
        val joystickX = 0.05
        val deadbandedX = joystickX.applyDeadband(0.1) // returns 0.0
        
        // Clamp motor output
        val motorOutput = 1.5.clamp(-1.0, 1.0) // returns 1.0
        
        // Scale sensor reading
        val encoderValue = 2048.0 // encoder counts
        val degrees = MathUtils.scaleRange(
            encoderValue, 
            0.0, 4096.0,  // encoder range
            0.0, 360.0    // degree range
        )
    }
    
    /**
     * Example of using geometry utilities.
     */
    fun geometryExample() {
        // Robot position
        val robotPosition = Point2D(1.5, 2.0)
        val targetPosition = Point2D(3.0, 4.5)
        
        // Calculate distance to target
        val distance = robotPosition.distanceTo(targetPosition)
        
        // Calculate direction vector
        val direction = (targetPosition - robotPosition).normalize()
        
        // Robot pose with rotation
        val robotPose = Pose2D(1.5, 2.0, 45.0.degreesToRadians())
        val targetPose = Pose2D(3.0, 4.5, 90.0.degreesToRadians())
        
        // Calculate angular error
        val angularError = robotPose.angularDistanceTo(targetPose)
        
        // Transform pose
        val relativePose = Pose2D(1.0, 0.0, 0.0) // 1 meter forward
        val newPose = robotPose.transformBy(relativePose)
    }
    
    /**
     * Example combining utilities for a simple controller.
     */
    fun combinedExample() {
        // PID configuration for rotation
        val rotationPid = Pingu(p = 0.1, i = 0.0, d = 0.01)
        
        // Current and target angles
        val currentAngle = 10.0.degreesToRadians()
        val targetAngle = 90.0.degreesToRadians()
        
        // Calculate error (shortest path)
        val error = MathUtils.angularDistance(currentAngle, targetAngle)
        
        // Apply PID control (simplified)
        val output = rotationPid.p * error
        
        // Clamp and apply deadband
        val finalOutput = output.clamp(-1.0, 1.0).applyDeadband(0.05)
        
        println("Rotation output: $finalOutput")
    }
}