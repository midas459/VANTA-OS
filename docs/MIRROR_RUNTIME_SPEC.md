# MIRROR — Virtual Environment / Runtime Specification

## Naming truth
MIRROR may have a workbench/sandbox mode, but call it a VM only if real virtualization is present and verified. Android app sandbox is not the same as a guest OS or hypervisor.

## Target phases
1. Runtime feasibility assessment on exact S10e model, Android release and CPU architecture.
2. Safe embedded/user-space runtime prototype with explicit scope (e.g., selected scripting or CLI tools) if compatible.
3. Sandboxed environment profile management.
4. Resource caps and lifecycle management.
5. Snapshot/restore and logs.
6. Evaluate genuine virtualization options only if hardware/software support is verified.

## Environment profile
- profile ID/name, runtime and version
- root directory
- memory/storage budget (enforced where possible)
- CPU/time limits where possible
- network mode: disabled / user-approved
- shared directories: explicit grants
- state: stopped / starting / running / paused / failed
- snapshot list and logs

## Controls
Create, start, stop, pause if supported, reset, snapshot, restore, inspect logs, delete with confirmation. Show limitations.

## Safety
- No automatic execution of unknown scripts/packages.
- Inspect code, package source and permissions before installing.
- No arbitrary host access.
- Shared files must be explicitly selected.
- Network access off by default for sandboxed experiments.
- Do not use environment to facilitate harmful or unauthorized activity.

## Acceptance criteria
- Environment start/stop verified on device.
- Resource limits actually enforced or limitations documented.
- User data separated from host app metadata.
- Reset/restore tests pass.
- Crash and interruption behavior tested.
- UI status reflects actual runtime, not mocked state.