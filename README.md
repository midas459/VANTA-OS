# VANTA OS

VANTA — Adaptive Intelligence Environment. Former codename: OVERMIND. Primary experience: THE VEIL.

VANTA is a phone-hosted intelligence/workspace environment designed initially for a Samsung Galaxy S10 family device. Android remains the host operating system; VANTA does not replace Samsung firmware.

## Android device build — 0.2.0

Native Kotlin + Jetpack Compose application.

- Application ID: `dev.vanta`
- Minimum Android API: 26
- Compile / target SDK: 35
- Java toolchain: 17
- UI: obsidian surfaces, restrained amber accents, touch-first scrolling layout
- Local DIRECTIVE parser: `help`, `status`, `task <title>`, `note <text>`, `list`
- Local task and note persistence in app-private storage
- Unit tests for parser behavior
- GitHub Actions workflow: Android build, lint, unit tests, and debug APK artifact

The command parser is deterministic and offline. It does not interpret arbitrary text as permission to control the phone. Module cards disclose which capabilities are implemented versus planned.

## Build and CI

The workflow is [Android CI](https://github.com/midas459/VANTA-OS/actions/workflows/android-ci.yml). It uses JDK 17, Android SDK 35 and Gradle 8.9, then runs:

```bash
gradle --no-daemon testDebugUnitTest lintDebug assembleDebug
```

**Verified:** [Android CI run #8 passed](https://github.com/midas459/VANTA-OS/actions/runs/44507356135) on commit `114270c`. The run passed unit tests, lint and debug APK assembly, then uploaded the `vanta-debug-apk` artifact (about 9 MB; expires 3 December 2026). Download it from the run's Artifacts section. This branch currently uses a provisioned Gradle distribution in CI; a Gradle wrapper is still a follow-up item for reproducible local command-line builds.

## Galaxy S10 installation

Follow [docs/GALAXY_S10_TEST.md](docs/GALAXY_S10_TEST.md). This is an ordinary APK installation. Do not root the phone, unlock its bootloader, repartition it, or flash firmware.

## Current truth

Implemented in source: THE VEIL UI, offline directive parser, local task/note capture, app-private persistence, parser unit tests, and CI workflow.

Only mark build/lint/tests as verified after the GitHub Actions run completes successfully. Physical S10 behavior remains unverified until the APK is installed and the device checklist is completed.

Not yet implemented: autonomous AI, remote model providers, app automation, encrypted memory, cloud sync, 200 GB storage allocation, genuine VM/runtime isolation, and firmware replacement. No paid API, hidden monitoring, or unchecked self-modification.

See [VANTA_MASTER_CONTEXT.md](VANTA_MASTER_CONTEXT.md), [ROADMAP.md](ROADMAP.md), and [docs](docs/).
