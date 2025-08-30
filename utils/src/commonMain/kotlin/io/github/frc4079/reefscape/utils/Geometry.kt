package io.github.frc4079.reefscape.utils

import kotlin.math.*

/**
 * Simple 2D point representation.
 */
data class Point2D(val x: Double, val y: Double) {
    /**
     * Calculates the distance to another point.
     */
    fun distanceTo(other: Point2D): Double {
        val dx = other.x - x
        val dy = other.y - y
        return sqrt(dx * dx + dy * dy)
    }
    
    /**
     * Calculates the squared distance to another point (more efficient when comparing distances).
     */
    fun distanceSquaredTo(other: Point2D): Double {
        val dx = other.x - x
        val dy = other.y - y
        return dx * dx + dy * dy
    }
    
    /**
     * Adds this point to another point.
     */
    operator fun plus(other: Point2D): Point2D = Point2D(x + other.x, y + other.y)
    
    /**
     * Subtracts another point from this point.
     */
    operator fun minus(other: Point2D): Point2D = Point2D(x - other.x, y - other.y)
    
    /**
     * Multiplies this point by a scalar.
     */
    operator fun times(scalar: Double): Point2D = Point2D(x * scalar, y * scalar)
    
    /**
     * Divides this point by a scalar.
     */
    operator fun div(scalar: Double): Point2D = Point2D(x / scalar, y / scalar)
    
    /**
     * Calculates the magnitude (length) of this point as a vector.
     */
    fun magnitude(): Double = sqrt(x * x + y * y)
    
    /**
     * Normalizes this point as a unit vector.
     */
    fun normalize(): Point2D {
        val mag = magnitude()
        return if (mag == 0.0) Point2D(0.0, 0.0) else Point2D(x / mag, y / mag)
    }
    
    /**
     * Calculates the dot product with another point.
     */
    fun dot(other: Point2D): Double = x * other.x + y * other.y
    
    /**
     * Rotates this point by an angle in radians.
     */
    fun rotate(angleRadians: Double): Point2D {
        val cos = cos(angleRadians)
        val sin = sin(angleRadians)
        return Point2D(x * cos - y * sin, x * sin + y * cos)
    }
    
    /**
     * Calculates the angle of this point in radians.
     */
    fun angle(): Double = atan2(y, x)
}

/**
 * Simple 2D pose representation (position + rotation).
 */
data class Pose2D(val x: Double, val y: Double, val rotationRadians: Double) {
    /**
     * Gets the position as a Point2D.
     */
    val position: Point2D get() = Point2D(x, y)
    
    /**
     * Gets the rotation in degrees.
     */
    val rotationDegrees: Double get() = rotationRadians.radiansToDegrees()
    
    /**
     * Calculates the distance to another pose.
     */
    fun distanceTo(other: Pose2D): Double = position.distanceTo(other.position)
    
    /**
     * Calculates the angular distance to another pose.
     */
    fun angularDistanceTo(other: Pose2D): Double = 
        MathUtils.angularDistance(rotationRadians, other.rotationRadians)
    
    /**
     * Transforms this pose by another pose.
     */
    fun transformBy(other: Pose2D): Pose2D {
        val cos = cos(rotationRadians)
        val sin = sin(rotationRadians)
        val newX = x + other.x * cos - other.y * sin
        val newY = y + other.x * sin + other.y * cos
        val newRotation = rotationRadians + other.rotationRadians
        return Pose2D(newX, newY, newRotation.normalizeAngle())
    }
    
    /**
     * Gets the inverse of this pose.
     */
    fun inverse(): Pose2D {
        val cos = cos(-rotationRadians)
        val sin = sin(-rotationRadians)
        val newX = -x * cos + y * sin
        val newY = -x * sin - y * cos
        return Pose2D(newX, newY, -rotationRadians)
    }
}

/**
 * Utility functions for geometry calculations.
 */
object GeometryUtils {
    /**
     * Calculates the area of a triangle given three points.
     */
    fun triangleArea(p1: Point2D, p2: Point2D, p3: Point2D): Double {
        return abs((p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)) / 2.0)
    }
    
    /**
     * Checks if a point is inside a triangle.
     */
    fun isPointInTriangle(point: Point2D, p1: Point2D, p2: Point2D, p3: Point2D): Boolean {
        val area = triangleArea(p1, p2, p3)
        val area1 = triangleArea(point, p2, p3)
        val area2 = triangleArea(p1, point, p3)
        val area3 = triangleArea(p1, p2, point)
        return abs(area - (area1 + area2 + area3)) < 1e-9
    }
    
    /**
     * Calculates the center point of a list of points.
     */
    fun centerPoint(points: List<Point2D>): Point2D {
        if (points.isEmpty()) return Point2D(0.0, 0.0)
        val sumX = points.sumOf { it.x }
        val sumY = points.sumOf { it.y }
        return Point2D(sumX / points.size, sumY / points.size)
    }
}