# Reefscape Utils - Multiplatform Library

This library provides platform-agnostic utility classes for FRC Team 4079's Reefscape robot and other platforms.

## Supported Platforms

- **JVM** - For robot code and desktop applications
- **iOS Native** - For iOS apps and frameworks (arm64, x64, simulatorArm64)

*Note: Android support can be added in the future by enabling the Android Gradle Plugin and adding the androidTarget configuration.*

## Installation

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("io.github.frc4079:utils:1.0.0")
}
```

### Gradle (Groovy DSL)
```groovy
dependencies {
    implementation 'io.github.frc4079:utils:1.0.0'
}
```

### Maven
```xml
<dependency>
    <groupId>io.github.frc4079</groupId>
    <artifactId>utils</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Available Classes

### Pingu - PID Controller Configuration
A platform-agnostic PID controller configuration class with optional feedforward gains.

```kotlin
// Basic PID controller
val basicPid = Pingu(p = 1.0, i = 0.0, d = 0.1)

// PID with feedforward
val advancedPid = Pingu(
    p = 1.0, 
    i = 0.0, 
    d = 0.1, 
    v = 0.7,    // velocity feedforward
    s = 0.5,    // static feedforward  
    g = 0.42    // gravity feedforward
)

// Check if feedforward gains are available
if (advancedPid.hasVelocityGain()) {
    val vGain = advancedPid.getVelocityGain()
}

// Create modified copies
val newPid = basicPid.withP(2.0).withI(0.1)
```

### MagicPingu - Motion Profile Configuration
Motion profiling constraints for velocity, acceleration, and jerk.

```kotlin
val profile = MagicPingu(
    velocity = 5.0,
    acceleration = 3.0, 
    jerk = 10.0
)

// Validate constraints
profile.validate() // throws if any value <= 0

// Create modified copy
val fasterProfile = profile.withVelocity(8.0)
```

### MathUtils - Mathematical Utilities
Common mathematical operations for robotics applications.

```kotlin
// Angle conversions
val radians = MathUtils.degreesToRadians(45.0)
val degrees = MathUtils.radiansToDegrees(Math.PI / 4)

// Angle normalization
val normalizedAngle = MathUtils.normalizeAngle(3 * Math.PI) // returns -PI

// Clamping and deadband
val clamped = MathUtils.clamp(15.0, 0.0, 10.0) // returns 10.0
val withDeadband = MathUtils.applyDeadband(0.05, 0.1) // returns 0.0

// Range scaling
val scaled = MathUtils.scaleRange(50.0, 0.0, 100.0, -1.0, 1.0) // returns 0.0

// Angular distance (shortest path)
val distance = MathUtils.angularDistance(0.0, 3 * Math.PI) // returns -PI

// Linear interpolation
val interpolated = MathUtils.lerp(0.0, 10.0, 0.5) // returns 5.0

// Epsilon equality
val isEqual = MathUtils.epsilonEquals(0.1, 0.10000001) // returns true
```

### Extension Functions
Convenient extension functions for common operations.

```kotlin
// Numeric extensions
val clamped = 15.0.clamp(0.0, 10.0)
val withDeadband = 0.05.applyDeadband(0.1)
val normalized = (3 * Math.PI).normalizeAngle()
val inDegrees = (Math.PI / 4).radiansToDegrees()
val inRadians = 45.0.degreesToRadians()

// Collection extensions
val safeItem = list.safeGet(10) // returns null if index out of bounds
val filtered = list.mapNotNull { transform(it) }
val (matching, nonMatching) = list.partition { predicate(it) }
```

### Geometry - 2D Geometry Classes
Simple 2D point and pose representations with useful operations.

```kotlin
// Points
val point1 = Point2D(3.0, 4.0)
val point2 = Point2D(0.0, 0.0)

val distance = point1.distanceTo(point2) // returns 5.0
val sum = point1 + point2
val scaled = point1 * 2.0
val magnitude = point1.magnitude() // returns 5.0
val unit = point1.normalize()

// Poses (position + rotation)
val pose = Pose2D(x = 1.0, y = 2.0, rotationRadians = Math.PI / 4)

val transformed = pose.transformBy(Pose2D(1.0, 0.0, 0.0))
val inverse = pose.inverse()
val angleDistance = pose.angularDistanceTo(otherPose)

// Geometry utilities
val center = GeometryUtils.centerPoint(listOf(point1, point2))
val area = GeometryUtils.triangleArea(point1, point2, Point2D(1.0, 1.0))
val inside = GeometryUtils.isPointInTriangle(testPoint, p1, p2, p3)
```

## Building and Publishing

### Building the Library
```bash
# Build all targets
./gradlew :utils:build

# Build specific target
./gradlew :utils:compileKotlinJvm
```

### Publishing Locally
```bash
# Publish to local Maven repository
./gradlew :utils:publishToMavenLocal
```

### Publishing to Maven Central
```bash
# Requires sonatype credentials in gradle.properties:
# sonatype.username=your-username
# sonatype.password=your-password

# Also requires GPG signing setup for production:
# signing.keyId=your-key-id
# signing.password=your-key-password  
# signing.secretKeyRingFile=path-to-secring.gpg

./gradlew :utils:publishAllPublicationsToSonatypeRepository
```

## iOS Integration

The library generates iOS frameworks for easy integration:

```swift
// In your iOS project, add the generated framework
import ReefscapeUtils

// Use the utility classes
let pidController = Pingu(p: 1.0, i: 0.0, d: 0.1, v: nil, s: nil, g: nil)
let angle = MathUtils.companion.degreesToRadians(degrees: 45.0)
```

## Development

### Project Structure
```
utils/
├── src/
│   ├── commonMain/kotlin/     # Platform-agnostic code
│   ├── jvmMain/kotlin/        # JVM-specific code
│   ├── androidMain/kotlin/    # Android-specific code
│   └── iosMain/kotlin/        # iOS-specific code
└── build.gradle               # Multiplatform configuration
```

### Adding New Utilities
1. Add classes to `src/commonMain/kotlin/` for platform-agnostic code
2. Add platform-specific implementations in respective source sets if needed
3. Write tests in `src/commonTest/kotlin/`
4. Update version in `build.gradle`
5. Publish new version

## License

This project is licensed under the WPILib License - see the [WPILib-License.md](../WPILib-License.md) file for details.