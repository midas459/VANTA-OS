# Galaxy S10 device test — VANTA 0.2.0

This is an APK installation test, not an operating-system replacement. Keep Samsung firmware and the Android bootloader untouched.

## 1. Get the APK

After the **Android CI** workflow succeeds on `vanta/android-foundation`:

1. Open the repository's [Actions page](https://github.com/midas459/VANTA-OS/actions).
2. Open the newest successful **Android CI** run for `vanta/android-foundation`.
3. Under Artifacts, download `vanta-debug-apk`.
4. Extract the ZIP. It contains `app-debug.apk`.

If the workflow fails, do not install an APK from that failed run. Read the failing job first.

## 2. Prepare the Galaxy S10

1. Charge the phone to at least 40%.
2. Keep a normal screen lock enabled.
3. Open Settings → About phone → Software information.
4. Tap **Build number** seven times; confirm your lock credentials if prompted.
5. Return to Settings → Developer options.
6. Enable **USB debugging** only if you plan to install from a computer. You can turn it off after testing.

Do not enable OEM unlocking, root the phone, flash firmware, or unlock the bootloader for this project.

## 3. Install

**Computer route (recommended):**

- Install Android Platform Tools (ADB) on your Windows PC.
- Connect the S10 by USB and accept the phone's RSA debugging prompt.
- In PowerShell, from the folder containing the APK, run:

```powershell
adb devices
adb install -r .\app-debug.apk
```

The device should appear as `device`, not `unauthorized`.

**Phone-only route:**

- Transfer `app-debug.apk` to the phone.
- Open it from My Files.
- If Android prompts you, allow My Files to install unknown apps for this installation, then install VANTA.
- Turn that permission back off afterwards if you do not need it.

## 4. First test checklist

- [ ] VANTA launches and stays open.
- [ ] THE VEIL fits the screen; scroll to reach the bottom.
- [ ] Enter `help`; supported commands are shown.
- [ ] Enter `task Test VANTA on my S10`; task count increases.
- [ ] Close and reopen VANTA; task remains.
- [ ] Enter `note First VANTA device test`; note is stored locally.
- [ ] Open MNEMOS; note appears.
- [ ] Turn on Airplane mode and repeat `status`; local commands still work.
- [ ] Rotate screen / return from another app; no crash.
- [ ] Android Settings → Apps → VANTA → Permissions shows no unexpected permissions.

## 5. Current capability boundary

Implemented in this build: local deterministic command parsing, task capture, note capture, app-private persistence, module status surfaces.

Not implemented: autonomous AI, app control, cloud sync, encrypted memory, 200 GB storage allocation, a virtual machine, or firmware replacement. The app does not perform arbitrary actions from free-form text.

## 6. Send back test results

Report the Android version shown in Settings → About phone → Software information, whether the app launches, which checklist items pass, and any exact error text or screenshot. Do not send passwords, lock codes, or private notes.
