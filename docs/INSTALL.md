# DIY Build and Install

## Requirements
- PC (Windows/macOS/Linux), Android Studio, USB data cable, phone with adequate free space.
- Install Android SDK components via Android Studio.

## Steps
1. Clone or download this repository: https://github.com/midas459/VANTA-OS
2. Open the project folder in Android Studio.
3. Let Gradle sync and install required SDK components.
4. Verify your phone's exact model, Android version and available space.
5. On the phone, Settings → About phone → Software information → tap Build number repeatedly to unlock Developer options (menus may differ).
6. Enable USB debugging in Developer options.
7. Connect phone and approve the debugging prompt only for your own computer.
8. Select the device in Android Studio and click Run to install.
9. Build APK using Android Studio's Build menu. Debug APK usually appears under app/build/outputs/apk/debug/.

## Troubleshooting
- Gradle sync: network, SDK installation and license prompts.
- Phone not listed: data cable, USB mode, debugging authorization.
- Install failure: check build errors, Android version compatibility and storage.
- Do not root, unlock bootloader, flash firmware or repartition for this project.

## Status
This repo is a reconstructed handover. Instructions are generic until Android source is committed and a build is verified. This is not an APK.