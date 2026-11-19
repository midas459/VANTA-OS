# VANTA OS

VANTA — Adaptive Intelligence Environment. Former codename: OVERMIND. Primary experience: THE VEIL.

VANTA is a phone-hosted intelligence/workspace environment designed initially for a Samsung Galaxy S10e. Android remains the host operating system; VANTA does not replace Android firmware.

## Android foundation (0.1.0)

The first source implementation is a native Kotlin + Jetpack Compose Android application.

- Application ID: `dev.vanta`
- Minimum Android API: 26
- Compile / target SDK: 35
- Java toolchain target: 17
- UI identity: obsidian surfaces, restrained amber accents, touch-first layout
- Current screen: THE VEIL foundation with module navigation and a directive input

The directive input captures input but does not yet execute missions. Module cards are navigation/status placeholders, not completed subsystems.

## Build

1. Install Android Studio with Android SDK Platform 35 and a JDK 17 runtime.
2. Open this repository in Android Studio.
3. Allow Gradle sync to complete.
4. Run the `app` configuration on an Android emulator or connected device.

A Gradle wrapper is not committed yet. Generate one from Android Studio or with Gradle 8.9 before relying on command-line builds.

## Current truth

This is an early Android foundation, not a complete Android OS, autonomous AI, verified VM, storage allocator, or tested APK. Physical Galaxy S10e validation and a reproducible wrapper-based build remain outstanding.

See [VANTA_MASTER_CONTEXT.md](VANTA_MASTER_CONTEXT.md), [ROADMAP.md](ROADMAP.md), and [docs](docs/). No root, firmware flashing, destructive installation, paid API calls, hidden monitoring, or unchecked self-modification.
