# GHOSTLINE — Android Integration Specification

## Scope
VANTA is an Android-hosted shell. Android and Samsung One UI remain the trusted host. Integrations use supported Android APIs and explicit user permissions.

## Current shell behavior
- MainActivity is registered as both a normal launcher activity and a selectable Android Home activity.
- The user may select VANTA as the default Home app in Android's Home app settings/chooser.
- If selected, the Android Home action returns to THE VEIL. The user can switch back to One UI Home in Settings.
- This does not replace the lock screen, system Settings, emergency UI, Android framework, kernel, bootloader, or Samsung firmware.

## Bixby key
The dedicated Bixby key is Samsung/OEM/firmware-controlled. A normal app cannot guarantee interception of its hardware event. Investigate supported remapping for the exact Galaxy S10 firmware and offer a visible launcher/shortcut fallback. Do not use hidden accessibility capture, key injection, root, or bypasses. Do not claim Bixby key support until verified on-device.

## Supported integration candidates
- Launch apps through resolved intents.
- Open Android Settings screens through supported intents.
- SAF folder/file selection and access.
- Share sheets and user-confirmed exports.
- Speech input/output only when activated and permissioned.
- Battery, connectivity, memory and storage status through supported APIs.
- Optional notification integration only with clear disclosure and explicit Notification Listener consent.

## Action registry
Every action declares ID, label, capability, intent/parameters, permission needs, preflight result, risk class, confirmation requirement, result/error, and audit event. Unsupported actions show unavailable rather than fake success.

## Accessibility and background work
Accessibility must be optional, narrowly scoped, user-enabled, disclosed and immediately disableable. Never silently collect screens or monitor activity. Background work respects Doze, battery limits and cancellation; use foreground service only when necessary with visible notification.

## Boot sequence
VANTA can show its own startup sequence after Android launches the selected Home activity. It cannot replace Samsung's pre-unlock boot animation or boot process without privileged firmware modification. Firmware modification is out of scope for the safe S10 development path.
