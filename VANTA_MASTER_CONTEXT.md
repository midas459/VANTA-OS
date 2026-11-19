# VANTA — MASTER CONTEXT / CONTINUATION FILE

**Read this before continuing development.** This is the canonical handover document for another AI or developer. It records vision, decisions, architecture, constraints, features and current status. Update it after implementation and tests.

## 1. Identity and intent
VANTA is a personal, phone-hosted adaptive intelligence environment—not simply an app launcher skin or chatbot. The user wants to instruct the phone to perform supported tasks and enter a distinct computing environment with missions, memory, resources, tools, and virtual workspaces.

Former codename: OVERMIND. Main environment: THE VEIL. First target: Samsung Galaxy S10e. Android remains the host OS. Do not propose rooting, flashing firmware, bootloader unlocking, repartitioning or destructive device changes as the normal install path.

## 2. UX requirements
- Touch-first, no physical keyboard requirement. Speech may be optional and permission-based.
- Dark obsidian/black design, restrained amber/gold accents, cinematic but legible.
- Mission-centric home and contextual navigation rather than a conventional app grid.
- Physical Bixby key ideally launches VANTA. This is OEM/firmware-dependent; provide a supported shortcut fallback and do not claim universal remapping.
- Buttons must execute real behavior; no fake toggles.
- User installs DIY through Android Studio/USB.
- Status indicators reflect actual subsystem state.

## 3. Named subsystems
| Module | Responsibility |
|---|---|
| VANTA | Overall environment |
| THE VEIL | Touch UI and workspace |
| SENTIENT CORE | Intent/context/planning orchestration |
| DIRECTIVE | Missions, tasks, dependencies, scheduler, retries, cancel |
| MNEMOS | Local knowledge, memory, provenance, search |
| INFINITE FABRIC | Resource telemetry and file workspace |
| MIRROR | Isolated virtual environments and simulation |
| GHOSTLINE | Approved Android integration/action adapters |
| ORACLE | AI/model/provider routing |
| WARDEN | Permissions, policies, confirmations, safety |
| BLACKBOX | Local audit/history |
| RECALL | Checkpoints, recovery, backup |
| EVOLUTION | Controlled module proposal/build/test/stage pipeline |

## 4. Engineering principles
1. Truthful feature status. A stub is not complete.
2. Free-first: native tools/offline/local first; free remote services only if allowed and user opts in. No paid service without explicit approval.
3. Offline-first; core notes, missions, logs, simple planning remain available offline.
4. User agency and cancel/stop control.
5. Least privilege; request permissions only when needed.
6. WARDEN policy checks actions. External/destructive/irreversible actions require review/confirmation.
7. Local-first privacy, export/delete controls, no telemetry by default.
8. Storage is not RAM. Flash-backed paging/cache is slower, causes flash wear and cannot replace physical memory.
9. Preserve Android and its security model.
10. Touch-first. Typing is optional; optional voice input must be transparent and permission-based.
11. Robustness: migration, crash recovery, checkpointing, low-memory handling, export/import.
12. Modules communicate through documented interfaces/events.
13. Generated code/modules go through DESIGN → BUILD → TEST → VALIDATE → STAGE → USER REVIEW → INSTALL; no unchecked live self-modification.

## 5. Host assumptions (verify)
- Samsung Galaxy S10e; exact model, region, Android version and hardware configuration unknown until device check.
- User reports about 512 GB accessible capacity; verify internal vs SD and free space.
- Touch display.
- Bixby key availability and remapping varies by firmware/Android version.
- Battery, background execution and thermal limits matter.
- Do not allocate all storage; keep headroom for Android and user files.

## 6. Feature specification

### THE VEIL — interface
- Adaptive mission-first home, command input (voice optional), recent missions, quick actions, health, workspace meter, AI provider status.
- Contextual module navigation and a cohesive identity.
- Accessible text/contrast, touch targets, responsive layout.
- Real status, no decorative fake capability.

### SENTIENT CORE — intelligence orchestrator
- Normalize requests, identify intent, entities, constraints, preferred outcome and required permissions.
- Assemble relevant context from selected mission and MNEMOS.
- Produce inspectable plans and tool selections.
- Scheduling, cancellation, bounded retry, evaluation and replanning.
- Show uncertainty and ask for missing input rather than fabricate.
- Begin with deterministic offline planner; model-based reasoning only when ORACLE is actually integrated.

### DIRECTIVE — mission/task engine
- Mission CRUD; states: draft, ready, running, paused, awaiting approval, failed, completed, canceled.
- Task/subtask graph, dependencies, priorities, due dates, retry limits, progress and result.
- Dependency-aware scheduler, timeouts, cancel, execution adapters.
- Checkpoint around actions; recovery after app/device interruption.
- Search, archive, tags, export/restore.
- Templates: study/research, coding, game development, design, planning, troubleshooting, generic.
- Plan/checklist must not be mistaken for automatic execution.

### ORACLE — AI provider routing
- Provider capability registry, health, model metadata, privacy/cost classes.
- Preference: native tools → local model if practical → authorized free remote provider with terms respected → optional user-supplied endpoint.
- No paid calls without explicit opt-in.
- Respect rate limits, terms and user eligibility. No quota/account rotation or bypass.
- Show fallback and offline mode.
- Never store credentials in source, repo, logs, plain preferences or exports. Use secure storage backed by Android Keystore when credentials are implemented.
- Not yet integrated.

### INFINITE FABRIC — workspace and resource control
- Observe app/system memory, available storage volumes, app usage, battery/thermal/network where supported.
- Workspace logical target 200 GB (configurable), NOT reserved disk and NOT RAM.
- User chooses directory through Android Storage Access Framework (SAF); persist access when supported.
- Areas: ACTIVE current files; CACHE rebuildable temp; VAULT durable context; ARCHIVE inactive items; MIRROR sandbox assets.
- Report actual bytes, target quota, real free space and projections; warn if capacity insufficient.
- Cache policy, integrity checks, backup, cleanup only of rebuildable cache with confirmation.
- Leave host OS storage headroom. Never claim ROM can become physical RAM.

### MIRROR — virtual environment
- Actual isolation/runtime is required; a VM screen alone does not count.
- Research Android-compatible options: sandboxed app, embedded runtime, user-space environment, supported virtualization library; document threat model and limits.
- Profiles, lifecycle, stop/reset, resource limits, snapshots and explicit file sharing.
- No automatic execution of unknown downloaded code.
- No real VM currently implemented.

### GHOSTLINE — Android action bridge
- App-launch intents and settings shortcuts for supported targets.
- SAF file-picker operations.
- Capability registry and preflight checks.
- Optional camera/microphone/sensors only with explicit permissions and user-visible purpose.
- Supported APIs only; no root assumed.
- Accessibility service, if considered, must be opt-in, transparent, user-enabled, narrowly scoped; never hidden monitoring.
- Actions show success/failure and confirmation as appropriate.
- Bixby launch behavior is device/firmware-dependent.

### MNEMOS — knowledge/memory
- Local-first notes, projects, entities/relations, tags, source, provenance, timestamp and versions.
- Categories: durable user-approved, active project, temporary session, unverified, archive.
- Search, edit, delete, import/export.
- Avoid unnecessary sensitive information.

### BLACKBOX — local event log
- Mission lifecycle, approvals, action invocation, result, errors, checkpoint/restore.
- Mark planned/simulated/executed/succeeded/failed distinctly.
- Redact secrets; retention/export/clear controls.

### RECALL — persistence/recovery
- Snapshots at safe boundaries.
- Detect interrupted work and offer restore.
- Undo only where supported; do not imply external actions are reversible.
- Versioned, validated backups; import merge semantics and overwrite protection.

### WARDEN — security
- Permission broker, risk categories, approval UX, sandbox policy, cancellation.
- Read-only, reversible local, external communication, destructive categories.
- Preview outbound content; user confirms irreversible/external actions.
- Safe defaults, audit, failure handling.

### EVOLUTION — controlled module lifecycle
- Module manifest/API, dependency/permission declarations, version and provenance.
- Pipeline: design/build/test/validate/stage/review/install.
- Rollback and tests.
- No live unreviewed self-modification or automatic privilege expansion.

## 7. End-to-end flow
1. Input through THE VEIL.
2. SENTIENT CORE classifies request and assembles allowed context.
3. DIRECTIVE creates inspectable task graph.
4. WARDEN checks permissions/risk.
5. ORACLE selects deterministic/local/authorized provider.
6. GHOSTLINE or MIRROR performs supported action.
7. Evaluate result; bounded retry or replan.
8. RECALL checkpoints state.
9. BLACKBOX logs provenance/outcome.
10. MNEMOS stores user-approved knowledge.

## 8. Status tracking discipline
Use labels: IMPLEMENTED (verified by test), PARTIAL, PLANNED, BLOCKED, UNVERIFIED. Update status only with evidence. A screen is not proof that a feature works. GitHub commit is not a release.

## 9. Current repository handover
Initial repo: midas459/VANTA-OS; public, default branch main; initially empty.
Previous conversation mentioned Android ZIP/source builds Genesis 1.0 through 2.2. Those artifact files were not available in this session's filesystem, so they could not be transferred byte-for-byte. This repo is reconstructed handover documentation and starter source, not a copy of those archives.
At initial handover: no APK, no Android build, no physical S10e validation, no verified AI integration, no real VM, no actual 200 GB allocator, no full device-control automation.

## 10. Instructions to the next AI
Read this file and the roadmap, architecture, security and install docs. Inspect current source and tests first. Do not restart design discovery. Prioritize a reproducible Android build, a real modular app, then small testable integrations. Keep the spec intact; update implementation status based on evidence. Do not claim unimplemented capabilities as complete.