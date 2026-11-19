# VANTA Architecture

## Layering
Presentation (THE VEIL) → orchestration (SENTIENT CORE) → mission/task graph (DIRECTIVE) → permission policy (WARDEN) → provider/tools (ORACLE) → Android adapters (GHOSTLINE) or isolated runtime (MIRROR). Persistence and audit cross-cut the system: MNEMOS, RECALL, BLACKBOX. Workspace and telemetry live in INFINITE FABRIC. EVOLUTION manages module lifecycle safely.

## Suggested package layout
- core/: orchestration interfaces, context, command types
- directive/: mission, task, dependencies, scheduler, execution
- oracle/: providers, capability descriptions, routing, consent
- ghostline/: Android intent/settings/action adapters
- mirror/: isolated runtime interface, profiles and lifecycle
- mnemos/: local knowledge, provenance, search
- fabric/: SAF workspace, quota and resource telemetry
- warden/: permissions, risk policy and approval
- blackbox/: event records and redaction
- recall/: checkpoints, restore, export/import
- evolution/: module manifest, pipeline and staging
- ui/: THE VEIL Compose surfaces

## Execution pipeline
Input → normalize intent → context selection → plan → validate dependencies → WARDEN preflight/approval → ORACLE provider selection → adapter/runtime → result evaluation → checkpoint → event log → optional user-approved memory.

## Contract
Mission/task plans are serializable. Tasks carry stable identifiers, descriptions, dependencies, priority, state, retry policy, timestamps and outcome. Execution adapters declare capabilities, required permissions, risk and cancellation support. Checkpoints and audit events occur around actions. UI observes domain state, not arbitrary button toggles.

## Storage
App-private database for core metadata. User-selected workspace through Android Storage Access Framework (SAF). Persist access grants where available. Atomic writes, schema migration, backups and integrity checks. The workspace quota is logical and never allocates beyond real capacity.

## ORACLE
Provider interfaces disclose capability, availability, privacy, cost class and terms. Native/offline first. Local model is optional after device benchmarking. Remote providers require user consent and legal/terms-compliant use. Credentials must use secure storage.

## Android integration
Use supported intents, SAF and permission APIs. Background work respects Android restrictions. Accessibility, if introduced, is optional, transparent and narrowly scoped. Hardware-key behavior varies by OEM/firmware.