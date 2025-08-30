# FIRST REEFSCAPE 2025

FRC 4079's repository for our 2025 robot code
![image](https://github.com/user-attachments/assets/5d3b9c7d-d495-4d81-bdd9-e715a5b2b35b)

## Table of Contents

- [Introduction](#introduction)
- [Multiplatform Utils Library](#multiplatform-utils-library)
- [Dependencies](#dependencies)
- [License](#license)

## Introduction

This repository contains the code for the FRC Team 4079's 2025 robot, named "Nautilus". The code is written in Java and Kotlin and is designed to run on the RoboRIO platform using WPILib. 

## Multiplatform Utils Library

This repository now includes a **multiplatform Kotlin library** that provides utility classes for use across different platforms:

### Supported Platforms
- **JVM** - For robot code and desktop applications  
- **iOS Native** - For iOS apps and frameworks

*Note: Android support can be easily added in the future when needed.*

### Available Utilities
- **Pingu** - Platform-agnostic PID controller configuration
- **MagicPingu** - Motion profiling constraints  
- **MathUtils** - Mathematical utilities for robotics
- **Geometry** - 2D geometry classes and utilities
- **Extensions** - Useful extension functions

### Installation
```kotlin
dependencies {
    implementation("io.github.frc4079:utils:1.0.0")
}
```

See the [utils README](utils/README.md) for detailed documentation and usage examples.

## Dependencies

The project relies on several dependencies, including:

- WPILib
- CTRE Phoenix
- GradleRIO
- Spotless
- Dokka

Dependencies are managed through the `build.gradle` file and the `vendordeps` directory.

## License

This project is licensed under the terms of the WPILib License. See the WPILib-License.md file for details.
