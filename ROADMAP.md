# VANTA Roadmap

Statuses: Implemented = coded and verified; Partial = incomplete; Planned = design-only; Unverified = source added but build/device test not yet demonstrated.

## 0 — Handover and Android foundation
- [x] Repository confirmed: midas459/VANTA-OS.
- [x] Master context, architecture, security and install documentation.
- [x] Native Kotlin/Jetpack Compose application module added on `vanta/android-foundation`.
- [x] THE VEIL initial touch UI and module selection shell added.
- [x] Directive input capture with honest not-connected status.
- [ ] Gradle wrapper and reproducible local command-line build.\n- [x] GitHub Actions CI workflow configured for Android build, lint, unit tests and APK artifact.\n- [x] Deterministic offline DIRECTIVE parser and local task/note persistence added.\n- [x] Galaxy S10 installation and manual test guide added.
- [ ] CI build, lint and unit tests pass (workflow configured; result pending).\n- [ ] Android Studio build and physical device tests.
- [ ] Install and test on physical Galaxy S10e.

## 1 — Android foundation
- [x] Kotlin/Compose project structure (build still unverified until CI completes).
- [x] THE VEIL shell/navigation foundation (device validation pending).
- [x] Initial status and module detail screens with implemented/planned capability labels.
- [ ] Lifecycle, low-memory, accessibility and Galaxy S10 layout testing.

## 2 — Persistence
- [ ] Database schema and migrations.
- [ ] Mission/task dependency model.
- [ ] Notes and local search.
- [ ] BLACKBOX audit.
- [ ] JSON export/import validation and merge.
- [ ] RECALL checkpoints.

## 3 — DIRECTIVE + SENTIENT CORE
- [ ] Intent parser and context assembly.
- [ ] Inspectable plan/task graph.
- [ ] Dependency-aware scheduler.
- [ ] Offline templates.
- [ ] Cancellation, timeouts, bounded retries.
- [ ] Evaluator/replanner interfaces.
- [ ] Real action-adapter registry.

## 4 — INFINITE FABRIC / 200 GB
- [ ] SAF-selected workspace.
- [ ] Persist directory permissions.
- [ ] Actual byte/quota meter.
- [ ] Configurable 200 GB target (logical quota, not reserved storage or RAM).
- [ ] ACTIVE/CACHE/VAULT/ARCHIVE/MIRROR folders.
- [ ] Low-space reserve and safeguards.
- [ ] Integrity/backup.
- [ ] Safe cleanup with user confirmation.
- [ ] RAM vs flash vs cache clarified.

## 5 — GHOSTLINE
- [ ] Android intent app launchers.
- [ ] Settings shortcuts.
- [ ] SAF file access.
- [ ] Capability registry and action preflight.
- [ ] Optional speech input/output.
- [ ] Permission-gated camera/mic/sensors.
- [ ] Accessibility feasibility and safeguards.
- [ ] Bixby-key launch research/fallback.

## 6 — ORACLE
- [ ] Provider/model registry.
- [ ] Offline/native tools.
- [ ] S10e local model benchmarks.
- [ ] Free remote providers within terms, explicit consent.
- [ ] Secure credential storage.
- [ ] Privacy display, rate limits, fallback.
- [ ] No paid API without user approval.

## 7 — MIRROR
- [ ] Android runtime/VM feasibility research.
- [ ] Isolated workbench.
- [ ] Environment profiles and lifecycle.
- [ ] Resource caps and file sharing.
- [ ] Snapshots/restore.
- [ ] Runtime installation safeguards.
- [ ] Isolation tests.
- [ ] Only call it VM when virtualization is genuine and verified.

## 8 — WARDEN
- [ ] Permission broker.
- [ ] Action risk categories.
- [ ] Approval flow.
- [ ] Stop/cancel.
- [ ] Audit/redaction.
- [ ] Recovery safeguards.

## 9 — EVOLUTION
- [ ] Module manifest/API.
- [ ] Proposal pipeline.
- [ ] Isolated build/test.
- [ ] Validation/staging/review/install.
- [ ] Versioning/rollback.
- [ ] No unchecked self-modification.

## 10 — Release quality
- [ ] End-to-end tests.
- [ ] S10e device testing.
- [ ] Battery, thermal, low-memory tests.
- [ ] Accessibility/permissions review.
- [ ] Backup/restore verified.
- [ ] APK and checksum.
- [ ] Install/rollback guide.
- [ ] Capability matrix.

## Definition of done
Build succeeds reproducibly; relevant tests pass; core workflows are tested on target device; permissions and safety reviewed; backups/restores verified; limitations documented. Repository contents alone do not prove a finished app.
