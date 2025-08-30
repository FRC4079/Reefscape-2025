package io.github.frc4079.reefscape.utils

/**
 * A platform-agnostic PID controller configuration class.
 * Contains proportional, integral, derivative, and optional feedforward gains.
 *
 * @property p The proportional gain.
 * @property i The integral gain.
 * @property d The derivative gain.
 * @property v The velocity feedforward gain (optional).
 * @property s The static/constant feedforward gain (optional).
 * @property g The gravity/position feedforward gain (optional).
 */
data class Pingu(
    var p: Double,
    var i: Double,
    var d: Double,
    var v: Double? = null,
    var s: Double? = null,
    var g: Double? = null,
) {
    /**
     * Gets the velocity feedforward gain.
     * @throws IllegalStateException if v is null
     */
    fun getVelocityGain(): Double = v ?: throw IllegalStateException("Velocity gain not set")

    /**
     * Gets the static feedforward gain.
     * @throws IllegalStateException if s is null
     */
    fun getStaticGain(): Double = s ?: throw IllegalStateException("Static gain not set")

    /**
     * Gets the gravity feedforward gain.
     * @throws IllegalStateException if g is null
     */
    fun getGravityGain(): Double = g ?: throw IllegalStateException("Gravity gain not set")

    /**
     * Returns true if velocity feedforward is configured.
     */
    fun hasVelocityGain(): Boolean = v != null

    /**
     * Returns true if static feedforward is configured.
     */
    fun hasStaticGain(): Boolean = s != null

    /**
     * Returns true if gravity feedforward is configured.
     */
    fun hasGravityGain(): Boolean = g != null

    /**
     * Creates a copy of this Pingu with updated PID values.
     */
    fun withPID(p: Double, i: Double, d: Double): Pingu =
        copy(p = p, i = i, d = d)

    /**
     * Creates a copy of this Pingu with an updated proportional gain.
     */
    fun withP(p: Double): Pingu = copy(p = p)

    /**
     * Creates a copy of this Pingu with an updated integral gain.
     */
    fun withI(i: Double): Pingu = copy(i = i)

    /**
     * Creates a copy of this Pingu with an updated derivative gain.
     */
    fun withD(d: Double): Pingu = copy(d = d)

    /**
     * Creates a copy of this Pingu with an updated velocity feedforward gain.
     */
    fun withV(v: Double): Pingu = copy(v = v)

    /**
     * Creates a copy of this Pingu with an updated static feedforward gain.
     */
    fun withS(s: Double): Pingu = copy(s = s)

    /**
     * Creates a copy of this Pingu with an updated gravity feedforward gain.
     */
    fun withG(g: Double): Pingu = copy(g = g)
}