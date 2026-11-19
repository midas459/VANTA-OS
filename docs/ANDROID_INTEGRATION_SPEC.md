# GHOSTLINE — Android Integration Specification

## Scope
VANTA can operate as a normal Android application. Android's security model and user permissions remain authoritative. Device control is limited to supported APIs and explicit user-granted access.

## Supported integration candidates
- Launch apps through resolved intents.
- Open Android Settings screens through supported intents.
- SAF folder/file selection and access.
- Share sheets and user-confirmed exports.
- Speech input and output where supported.
- Camera/mic/sensors only when the user activates feature and grants permission.
- Battery, connectivity, memory and storage status through supported APIs.
- Optional notification integration only with clear disclosure and user-granted Notification Listener access.

## Bixby key
- Hardware-key behavior is OEM/firmware-dependent.
- Investigate Samsung's available settings and third-party remapping compatibility for the exact firmware.
- Prefer supported app shortcuts/launcher affordances as fallback.
- Never promise the app can intercept the button unconditionally.
- No root, key injection, firmware modifications, or bypasses.

## Action registry
Every action describes: ID, label, capability, intent/parameters, permission needs, preflight result, risk class, confirmation requirement, result/error, audit event. Unsupported actions must show unavailable, not fake success.

## Accessibility
If an accessibility integration is considered:
- Explain its scope and purpose.
- User enables it manually in Android settings.
- Restrict it to documented workflows.
- Provide immediate disable path.
- Never silently collect screens or monitor user activity.
- No hidden remote-control behavior.

## Background work
Use Android-supported background scheduling. Respect Doze, battery and task limits. Foreground service only when task genuinely requires it and disclosure/notification are provided. Work can be interrupted and must checkpoint safely.