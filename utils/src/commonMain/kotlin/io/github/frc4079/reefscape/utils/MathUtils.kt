package io.github.frc4079.reefscape.utils

import kotlin.math.*

/**
 * Platform-agnostic mathematical utilities for robotics applications.
 */
object MathUtils {
    
    /**
     * Converts degrees to radians.
     */
    fun degreesToRadians(degrees: Double): Double = degrees * PI / 180.0
    
    /**
     * Converts radians to degrees.
     */
    fun radiansToDegrees(radians: Double): Double = radians * 180.0 / PI
    
    /**
     * Clamps a value between a minimum and maximum.
     */
    fun clamp(value: Double, min: Double, max: Double): Double {
        return when {
            value < min -> min
            value > max -> max
            else -> value
        }
    }
    
    /**
     * Applies a deadband to a value.
     * Values within the deadband are returned as 0.0.
     */
    fun applyDeadband(value: Double, deadband: Double): Double {
        return if (abs(value) > abs(deadband)) value else 0.0
    }
    
    /**
     * Scales input from one range to another.
     */
    fun scaleRange(
        value: Double,
        fromMin: Double,
        fromMax: Double,
        toMin: Double,
        toMax: Double
    ): Double {
        val fromRange = fromMax - fromMin
        val toRange = toMax - toMin
        val scaledValue = (value - fromMin) / fromRange
        return toMin + (scaledValue * toRange)
    }
    
    /**
     * Normalizes an angle to be within [-PI, PI] radians.
     */
    fun normalizeAngle(angle: Double): Double {
        var normalized = angle
        while (normalized > PI) normalized -= 2 * PI
        while (normalized < -PI) normalized += 2 * PI
        return normalized
    }
    
    /**
     * Normalizes an angle to be within [-180, 180] degrees.
     */
    fun normalizeAngleDegrees(angle: Double): Double {
        var normalized = angle
        while (normalized > 180) normalized -= 360
        while (normalized < -180) normalized += 360
        return normalized
    }
    
    /**
     * Calculates the shortest angular distance between two angles in radians.
     */
    fun angularDistance(from: Double, to: Double): Double {
        return normalizeAngle(to - from)
    }
    
    /**
     * Calculates the shortest angular distance between two angles in degrees.
     */
    fun angularDistanceDegrees(from: Double, to: Double): Double {
        return normalizeAngleDegrees(to - from)
    }
    
    /**
     * Linear interpolation between two values.
     */
    fun lerp(start: Double, end: Double, t: Double): Double {
        return start + t * (end - start)
    }
    
    /**
     * Checks if two values are equal within a tolerance.
     */
    fun epsilonEquals(a: Double, b: Double, epsilon: Double = 1e-9): Boolean {
        return abs(a - b) < epsilon
    }
    
    /**
     * Calculates the hypotenuse of a right triangle.
     */
    fun hypot(x: Double, y: Double): Double = sqrt(x * x + y * y)
    
    /**
     * Calculates the 3D distance between two points.
     */
    fun distance3D(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
        val dx = x2 - x1
        val dy = y2 - y1
        val dz = z2 - z1
        return sqrt(dx * dx + dy * dy + dz * dz)
    }
}