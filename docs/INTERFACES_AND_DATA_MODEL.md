# VANTA Interfaces and Data Model

This document proposes stable contracts. Keep Android/UI implementation details out of domain models where possible. The schema is illustrative; implementation may evolve with migration notes.

## Core domain entities

### Mission
- id: stable UUID
- title, description, createdAt, updatedAt
- status: draft | ready | running | paused | waitingApproval | failed | completed | canceled
- tags, priority, optional dueAt
- taskIds, progress derived from task states
- selectedProject/workspace context
- lastCheckpointId, outcome, errorSummary

### Task
- id, missionId, title, description
- status: pending | ready | running | waiting | blocked | succeeded | failed | canceled
- dependencies: task IDs
- priority, optional dueAt
- retryLimit, retryCount, timeout
- requiredCapabilities
- riskClass: readOnly | localReversible | external | destructive
- input references, output references, timestamps, result/error

### DirectivePlan
- id, missionId, createdAt
- source: offlinePlanner | modelProvider
- planVersion, rationaleSummary
- ordered task IDs and dependency graph
- status: proposed | approved | executing | paused | complete | rejected
- required approvals and assumptions

### KnowledgeItem
- id, title, body, tags, category
- createdAt, updatedAt, source/provenance
- confidence/verification state
- related entity IDs, revision

### Workspace
- id, user-selected directory URI
- display name, persisted permission flags
- targetQuotaBytes (default logical 200 GB)
- actualUsedBytes, actualFreeBytes last measured
- area definitions: ACTIVE, CACHE, VAULT, ARCHIVE, MIRROR
- scan timestamps, exclusions, status

### ActionRequest
- id, action type, parameters (typed and validated)
- declared capabilities, permission requirements
- risk classification, preview
- requestedAt, expiry, approval state

### ActionResult
- actionRequestId, status: blocked | canceled | attempted | succeeded | failed
- result summary, safe output reference
- start/end timestamps, error code
- provenance and checkpoint references

### ProviderDescriptor
- providerId, displayName, modelId
- capability list, availability, health
- privacyClass, costClass, rateLimit description
- context limits, consent status
- supported modalities

### Checkpoint
- id, missionId, createdAt, schemaVersion
- state snapshot reference and integrity hash
- restoration eligibility and notes

### EventRecord
- id, timestamp, event type
- actor: user | core | adapter | provider
- target entity ID
- status: planned | simulated | attempted | succeeded | failed | blocked
- redacted details and provenance

### ModuleManifest
- moduleId, version, description, entrypoint/API version
- dependencies, permissions, capabilities
- integrity/hash, source/provenance, validation status
- lifecycle: proposed | building | testing | staged | approved | installed | disabled | rolledBack

## Module boundaries
- THE VEIL consumes state flows and dispatches user intent; no direct file/network/device control.
- SENTIENT CORE orchestrates planning and context.
- DIRECTIVE owns mission/task lifecycle and scheduler.
- WARDEN is consulted before execution.
- ORACLE exposes model/provider capability selection and consent.
- GHOSTLINE owns Android action adapters.
- MIRROR owns sandbox/runtime lifecycle.
- MNEMOS owns knowledge storage and retrieval.
- INFINITE FABRIC owns workspace and telemetry.
- BLACKBOX owns audit event recording.
- RECALL owns checkpoint/backup operations.
- EVOLUTION owns module validation/staging.

## Execution interface concept
An adapter should declare:
- adapterId and version
- supported action types
- required capabilities and permissions
- risk class
- cancellability and timeout support
- preflight(request) returning allowed/blocked/needsApproval and rationale
- execute(request, approvedContext) returning typed result
- cancel(requestId) when supported

## Data integrity
- Stable IDs; timestamps in UTC.
- Version every persisted/exported schema.
- Validate imported data, size limits, and referenced IDs.
- Avoid secrets in models.
- Atomic local writes and backups before migrations.
- Unknown fields should be handled safely when feasible.
- Merge import by IDs; conflict preview and explicit resolution.

## Event and state rules
- State transitions validated by engine, not UI.
- No task runs before dependencies succeed.
- No adapter runs before WARDEN approval when needed.
- Status distinctions must be visible.
- Retry only within policy.
- Every execution attempt produces an audit event.
- Completion requires task results, not just UI interaction.