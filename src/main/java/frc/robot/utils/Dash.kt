package frc.robot.utils

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.util.WPISerializable
import edu.wpi.first.util.struct.StructSerializable
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.utils.RobotParameters.SwerveParameters.Thresholds
import org.littletonrobotics.junction.Logger
import java.util.function.DoubleConsumer

/**
 * Utility class for interacting with the SmartDashboard and logging. Provides methods to update PID
 * values, retrieve double values, create pairs, and perform test logging.
 */
object Dash {
    /**
     * Method to update PIDV values from the SmartDashboard.
     *
     * @param pid The PID object to update.
     * @param velocity The velocity to update.
     * @param prefix The prefix for the SmartDashboard keys.
     * @param changeV The function to change the velocity.
     */
    @JvmStatic
    fun dashPID(
        prefix: String,
        pid: PIDController,
        velocity: Double,
        changeV: DoubleConsumer,
    ) {
        pid.p = SmartDashboard.getNumber("$prefix Auto P", pid.p)
        pid.i = SmartDashboard.getNumber("$prefix Auto I", pid.i)
        pid.d = SmartDashboard.getNumber("$prefix Auto D", pid.d)
        changeV.accept(SmartDashboard.getNumber("$prefix Auto V", velocity))
    }

    /**
     * Logs a double value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The double value to log.
     */
    @JvmStatic
    fun log(
        key: String?,
        value: Double,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, value)
        }
    }

    /**
     * Logs an integer value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The integer value to log.
     */
    @JvmStatic
    fun log(
        key: String?,
        value: Int,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, value)
        }
    }

    /**
     * Logs a boolean value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The boolean value to log.
     */
    @JvmStatic
    fun log(
        key: String?,
        value: Boolean,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, value)
        }
    }

    /**
     * Logs a String value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The String value to log.
     */
    @JvmStatic
    fun log(
        key: String?,
        value: String?,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, value)
        }
    }

    /**
     * Logs a WPISerializable value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The WPISerializable value to log.
     */
    @JvmStatic
    fun <T : WPISerializable?> log(
        key: String?,
        value: T,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, value)
        }
    }

    /**
     * Logs a SwerveModuleState value with a specified key if the system is in test mode.
     *
     * @param key The key associated with the value to log.
     * @param value The SwerveModuleState value to log.
     */
    @JvmStatic
    fun <T : StructSerializable?> log(
        key: String?,
        vararg value: T,
    ) {
        if (Thresholds.TEST_MODE) {
            Logger.recordOutput(key, *value)
        }
    }
}
