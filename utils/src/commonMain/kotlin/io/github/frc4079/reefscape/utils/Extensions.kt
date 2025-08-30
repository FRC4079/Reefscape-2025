package io.github.frc4079.reefscape.utils

/**
 * Platform-agnostic extension functions for common operations.
 */

/**
 * Extension function to clamp a Double value between min and max.
 */
fun Double.clamp(min: Double, max: Double): Double = MathUtils.clamp(this, min, max)

/**
 * Extension function to apply deadband to a Double value.
 */
fun Double.applyDeadband(deadband: Double): Double = MathUtils.applyDeadband(this, deadband)

/**
 * Extension function to normalize an angle in radians.
 */
fun Double.normalizeAngle(): Double = MathUtils.normalizeAngle(this)

/**
 * Extension function to normalize an angle in degrees.
 */
fun Double.normalizeAngleDegrees(): Double = MathUtils.normalizeAngleDegrees(this)

/**
 * Extension function to convert degrees to radians.
 */
fun Double.degreesToRadians(): Double = MathUtils.degreesToRadians(this)

/**
 * Extension function to convert radians to degrees.
 */
fun Double.radiansToDegrees(): Double = MathUtils.radiansToDegrees(this)

/**
 * Extension function to check if this Double is approximately equal to another.
 */
fun Double.epsilonEquals(other: Double, epsilon: Double = 1e-9): Boolean = 
    MathUtils.epsilonEquals(this, other, epsilon)

/**
 * Extension function to safely get an item from a list at an index, returning null if out of bounds.
 */
fun <T> List<T>.safeGet(index: Int): T? = if (index in indices) this[index] else null

/**
 * Extension function to get the first element that satisfies a predicate, or null if none found.
 */
inline fun <T> List<T>.firstOrNull(predicate: (T) -> Boolean): T? {
    for (element in this) {
        if (predicate(element)) return element
    }
    return null
}

/**
 * Extension function to map a list and filter out null results.
 */
inline fun <T, R> List<T>.mapNotNull(transform: (T) -> R?): List<R> {
    val result = mutableListOf<R>()
    for (element in this) {
        val transformed = transform(element)
        if (transformed != null) {
            result.add(transformed)
        }
    }
    return result
}

/**
 * Extension function to partition a list into two lists based on a predicate.
 */
inline fun <T> List<T>.partition(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val first = mutableListOf<T>()
    val second = mutableListOf<T>()
    for (element in this) {
        if (predicate(element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}

/**
 * Extension function to check if all elements in a list satisfy a predicate.
 */
inline fun <T> List<T>.all(predicate: (T) -> Boolean): Boolean {
    for (element in this) {
        if (!predicate(element)) return false
    }
    return true
}

/**
 * Extension function to check if any element in a list satisfies a predicate.
 */
inline fun <T> List<T>.any(predicate: (T) -> Boolean): Boolean {
    for (element in this) {
        if (predicate(element)) return true
    }
    return false
}