# VANTA Complete Feature Catalog

This document describes the full intended product. Every item is aspirational unless marked verified in the implementation and test records. This catalog is the scope reference for feature development.

## Product promise
VANTA is an Android-hosted, touch-first, offline-capable personal computing environment that transforms tasks into plans, organizes knowledge and files, and executes supported phone actions under user control. It must be useful without remote AI or internet. The first host is the Samsung Galaxy S10e.

## THE VEIL — experience layer
- Mission-first home, not an ordinary app grid.
- Central directive composer with optional voice capture.
- Recent, active, paused, awaiting-approval, and completed missions.
- Context-aware module cards and quick actions.
- Workspace meter based on measured volume capacity and actual usage.
- Resource health: memory estimates, storage, battery, thermal/network signals when APIs allow.
- Offline/online and AI-provider status, with clear labels.
- Touch-friendly large targets; keyboard is optional.
- Dark obsidian theme, amber accent, restrained animation, accessibility settings.
- Optional lock/quick-launch affordance; app shortcut fallback if Bixby key cannot be remapped.
- UI must not show fictional system activity.

## SENTIENT CORE — command orchestration
- Interpret directive text or speech; clarify ambiguous requests.
- Intent, entity, constraint, priority, deadline, desired output, permission and risk extraction.
- Context retrieval limited to relevant selected mission/workspace and user-approved memory.
- Plan generation; task graph and dependency validation.
- Tool selection based on actual capabilities.
- Execution monitoring, progress, cancellation, bounded retries, evaluation and replanning.
- Offline deterministic reasoning as fallback.
- Remote model reasoning only through ORACLE and explicit consent.

## DIRECTIVE — mission system
- Create, edit, clone, tag, search, archive, pause, resume, cancel and complete missions.
- Task/subtask graph with dependency edges, priority, due dates, notes, outputs and retry policies.
- State machine and transition validation.
- Estimate progress from task states, not arbitrary progress bars.
- Scheduling and execution queue.
- Preflight permission/capability checks.
- Waiting-for-user and waiting-for-approval states.
- Timeouts and bounded retries.
- Checkpointing, failure summaries, recovery and resume.
- Templates for school/study, research, coding, game development, design, troubleshooting, personal planning and general goals.
- Export mission as portable bundle; import with validation and merge.

## MNEMOS — knowledge fabric
- Notes and knowledge cards.
- Project-specific memory and relationships between entities.
- Tags, categories, source, timestamps, confidence, verification state and revision history.
- Search and filters.
- Active, durable, temporary, unverified and archived memory classes.
- Explicit save-to-memory and delete/edit controls.
- Import/export and backup.
- No hidden collection of sensitive data.

## INFINITE FABRIC — storage and resource management
- Select a workspace folder or volume through Android SAF.
- Logical workspace target default: 200 GB; configurable.
- Actual capacity, available space and allocated/used bytes measured separately.
- Workspace areas: ACTIVE, CACHE, VAULT, ARCHIVE, MIRROR.
- Per-area quotas, file inventory and size reporting.
- Optional project workspace structure and manifest.
- Integrity checks, duplicate detection (future), indexing with exclusions.
- Cleanup recommendations; CACHE-only automated cleanup if validated; delete/archive requires user confirmation.
- Low-storage warnings and reserve for Android/user data.
- Export/import, checksums, interrupted-write recovery.
- Storage-backed caches/checkpoints are not physical RAM; never claim ROM-to-RAM conversion or unlimited memory.

## MIRROR — virtual lab
- Runtime discovery and compatibility report.
- Environment profiles with name, runtime, root directory, resource caps, networking policy and shared folders.
- Start/stop/reset, logs, status, snapshot/restore.
- Isolated user-space environment first if feasible.
- Separate VM/hypervisor is a later possibility and must be technically verified on the target.
- No automatic execution of unknown packages or scripts.
- Explicit shared-file access; sandbox boundaries documented.
- If true virtualization cannot run on S10e, offer an honestly named sandbox/workbench rather than mislabeling it VM.

## GHOSTLINE — Android bridge
- Launch user-selected installed apps using supported intents.
- Open supported settings panels.
- File picker and user-granted file operations.
- Clipboard actions only with user interaction and OS limits.
- Optional speech-to-text and text-to-speech.
- Optional camera, microphone, sensors, location only on feature invocation and with permission.
- Network connectivity status; no covert monitoring.
- Capability registry with supported action descriptions.
- Preflight, confirmation, outcome and error feedback.
- Android restrictions documented. No root or hidden controls.
- Accessibility service is optional, visible, user-enabled and narrowly scoped; never a hidden remote-control mechanism.

## ORACLE — intelligence routing
- Provider/model catalog and capabilities.
- Offline/native capability route.
- Local model route if benchmark proves practical.
- Optional free remote route only with user consent, service terms and rate limits respected.
- Provider health checks, context limits, privacy disclosure and failure fallback.
- Route selection based on task, privacy, capability, latency and user preference.
- Usage display and per-provider disable control.
- No paid API unless explicitly approved.
- Secure secrets storage; never commit tokens.
- No quota evasion, account cycling or policy bypass.

## WARDEN — action policy and safety
- Action classification: read-only, reversible local, external, destructive.
- Capability allow-list and permission broker.
- Preflight and permission rationale.
- Confirm external messages, sharing, purchases, publication and irreversible actions.
- Display action plan and destination/content before confirmation.
- Cancel/stop control.
- Rate limits, timeouts and bounded retries.
- Audit record; secret redaction.
- Safe failure and recovery.

## BLACKBOX — history
- Timeline of mission changes, plan approvals, tool calls, results, errors and recovery.
- Event provenance: planned, simulated, attempted, succeeded, failed, blocked.
- Local-only by default; retention controls.
- Search, filter, export and clear.
- Never log tokens, passwords or unnecessary private content.

## RECALL — restore
- Mission checkpoints and workspace manifests.
- Crash/interruption detection.
- Restore previous local state where safe.
- Export/import backups with schema version and validation.
- Default merge import; explicit overwrite only after preview and backup.
- Integrity checks and migration support.
- Undo only where an action is actually reversible.

## EVOLUTION — module lifecycle
- Module manifest with ID, version, description, dependencies, capabilities, permission declarations, provenance.
- Module proposals generated from specs.
- Build, tests, static validation, isolated staging and review.
- Version history, rollback and dependency checks.
- User approval before installing/updating.
- No unchecked live self-modification or automatic privilege escalation.

## Settings and administration
- Privacy controls, permissions summary, provider preferences, offline mode, workspace volume/quota, theme/accessibility, logs, backup/restore, module versions, diagnostics, support export with redaction.

## Quality attributes
- Responsive, battery-conscious, low-memory tolerant.
- Robust offline.
- Predictable cancellation.
- Accessible and readable.
- Storage-safe.
- Clear failures and recovery.
- Testable modular architecture.
- No feature is considered complete merely because a UI entry exists.