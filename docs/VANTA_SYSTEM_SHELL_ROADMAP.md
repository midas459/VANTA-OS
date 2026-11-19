# VANTA system-shell roadmap

## Target experience

VANTA should be the user's primary interaction shell on the Galaxy S10, while Samsung Android remains the trusted host OS. This is a user-space shell project, not an attempt to replace the phone's bootloader, kernel, vendor firmware or recovery partition.

## Capability ladder

### Stage 1 — Android-hosted shell (current implementation)
- [x] Kotlin/Compose THE VEIL interface.
- [x] Register VANTA as a selectable Android Home activity.
- [x] Keep the ordinary launcher entry so the user can open it without changing defaults.
- [x] App-private workspace directory and quota accounting.
- [x] Report actual internal-volume capacity separately from the 200 GiB target.
- [ ] Verify the HOME chooser and default-home behavior on a physical Galaxy S10.
- [ ] Emulator UI smoke tests pass.

When the user selects VANTA as the default Home app, pressing the Android Home button returns to THE VEIL. Android Settings can restore One UI Home at any time. The lock screen, Settings, emergency UI, system navigation and Android security remain Samsung/Android-owned.

### Stage 2 — Hardware-key integration
The dedicated Bixby key is Samsung/OEM/firmware-controlled. A normal third-party app cannot guarantee interception of its key event. Investigate exact firmware's built-in key remapping and user-approved shortcut mechanisms. Never promise universal support, use hidden accessibility capture, inject keys, or bypass Samsung restrictions. If unavailable, provide a visible VANTA shortcut and optionally support a supported assistant/shortcut role where the firmware allows it.

### Stage 3 — Startup identity
VANTA may show its own startup sequence after Android launches the selected Home activity. It cannot replace Samsung's pre-unlock boot animation, bootloader or system startup without privileged firmware-level changes. Do not flash a custom ROM as part of the normal S10 test path.

### Stage 4 — Dedicated runtime
MIRROR must begin as a constrained, disposable user-space runtime (for example, a process/container-like workspace with explicit resource limits). It must not be described as a VM until isolation boundaries are demonstrated and tested. Do not run untrusted payloads on the host.

## Storage model — INFINITE FABRIC

The 200 GiB number is a logical workspace ceiling, not a partition, reservation or promise that 200 GiB is available. Current implementation creates `files/vanta-fabric` inside VANTA's Android app-private sandbox. It reports:
- logical target quota: 200 GiB;
- measured workspace bytes;
- actual free and total capacity on the internal app-data volume;
- 2 GiB safety headroom that future writes must preserve.

Every write path must call `WorkspaceQuota.canAcceptWrite` before creating data, then use temporary files and atomic commit where supported. Current implementation provides the quota model and measurement UI; no general file import/export/write manager is claimed yet. Android may still reclaim storage or reject writes, so all I/O must handle ENOSPC and permission failures.

App-private data is isolated from ordinary apps by Android's sandbox, but it is not yet a separately encrypted Knox Secure Folder. Encryption at rest using Android Keystore is a separate milestone. `allowBackup=false` avoids Android app auto-backup for this initial private workspace. Uninstalling VANTA deletes its app-private workspace.

## RAM, swap and ROM

- Physical RAM remains controlled by Android/Linux.
- Android may use compressed RAM (zRAM), configured by the OS/kernel.
- A ROM is firmware/system image storage, not extra RAM.
- VANTA must not create a storage-backed swapfile or claim that flash storage adds RAM. Flash is far slower than RAM and swap increases write wear.
- VANTA can reduce memory pressure through bounded caches, streaming I/O, background work limits and cache eviction.

## Safety and recovery

- No root, bootloader unlock, repartition, custom recovery or firmware flashing for Stage 1.
- Preserve One UI Home as the recovery/default alternative.
- No silent scanning of unrelated folders.
- No destructive cleanup without explicit confirmation.
- Always distinguish measured capacity, quota target, reserved headroom and actual usage.
