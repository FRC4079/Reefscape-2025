package io.github.frc4079.reefscape.utils

/**
 * A platform-agnostic motion profile configuration class.
 * Contains velocity, acceleration, and jerk constraints for motion profiling.
 *
 * @property velocity The maximum velocity.
 * @property acceleration The maximum acceleration.
 * @property jerk The maximum jerk.
 */
data class MagicPingu(
    var velocity: Double,
    var acceleration: Double,
    var jerk: Double,
) {
    /**
     * Creates a copy of this MagicPingu with updated velocity.
     */
    fun withVelocity(velocity: Double): MagicPingu = copy(velocity = velocity)

    /**
     * Creates a copy of this MagicPingu with updated acceleration.
     */
    fun withAcceleration(acceleration: Double): MagicPingu = copy(acceleration = acceleration)

    /**
     * Creates a copy of this MagicPingu with updated jerk.
     */
    fun withJerk(jerk: Double): MagicPingu = copy(jerk = jerk)

    /**
     * Validates that all values are positive.
     * @throws IllegalArgumentException if any value is not positive
     */
    fun validate() {
        require(velocity > 0) { "Velocity must be positive, got $velocity" }
        require(acceleration > 0) { "Acceleration must be positive, got $acceleration" }
        require(jerk > 0) { "Jerk must be positive, got $jerk" }
    }

    /**
     * Returns true if all values are valid (positive).
     */
    fun isValid(): Boolean = velocity > 0 && acceleration > 0 && jerk > 0
}