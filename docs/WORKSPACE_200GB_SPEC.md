# INFINITE FABRIC — 200 GiB Workspace Specification

## Goal and current implementation
The user wants a dedicated 200 GiB VANTA workspace on a 512 GB Galaxy S10. This is a logical quota target, not a partition, reserved block, or RAM expansion.

Current implementation:
- Creates `files/vanta-fabric` inside VANTA's Android app-private sandbox.
- Displays target quota (200 GiB), measured workspace usage, and actual free/total capacity on the app-data internal volume.
- Reserves 2 GiB of free-space headroom from future VANTA writes.
- Includes a quota guard that checks both remaining logical quota and real free capacity.
- Does not yet provide a general file manager, import/export, or encrypted vault.

## What the number means
1. Target workspace quota: 200 GiB.
2. Actual directory content size: measured recursively.
3. Internal volume total and free bytes: measured using StatFs on the app data volume.
4. Physical RAM: separate metric; never mix it with storage figures.

A 200 GiB target is not a claim that 200 GiB is currently free. Every future write path must check quota and available capacity. Android may reject writes, so all I/O must handle storage-full and interruption errors.

## Privacy and safe-folder boundary
App-private storage is protected by Android's per-app sandbox. It is not the same as Samsung Knox Secure Folder and is not yet separately encrypted by VANTA. Android Keystore-backed encryption is a future requirement before claiming a VANTA encrypted vault. Auto-backup is disabled in the current manifest; uninstalling the app removes its private workspace.

## Future expansion
- SAF-based user-selected storage locations, with explicit consent and persisted grants.
- Vault encryption using Android Keystore, versioned file format and recovery/export flow.
- ACTIVE, CACHE, VAULT, ARCHIVE and MIRROR areas with separate quotas.
- Atomic writes, integrity hashes, resumable operations and low-space warnings.
- Storage benchmark and full-device test on Galaxy S10.
- Never silently scan unrelated folders or delete user data.

## RAM distinction
RAM is volatile physical memory. Android/Linux manages memory pressure and zRAM. Flash storage is not RAM; using it as swap is slow and increases write wear. VANTA should use bounded caches, streaming I/O and graceful cache eviction instead of claiming storage-backed RAM.
