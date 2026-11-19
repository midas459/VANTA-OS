# VANTA Security & Privacy

- Offline-first and no telemetry by default.
- Least-privilege permission requests at point of use.
- No secrets in source, Git, logs, exports, or plain preferences.
- User can cancel missions and inspect actions.
- External and destructive actions require review and confirmation.
- AI-generated modules must be tested and staged before user-approved installation.

## Action classes
- Read-only local inspection.
- Reversible local changes (undo/atomic save).
- External/communication (preview and approval).
- Destructive (impact display, confirmation and recovery path).

## Workspace
Never reserve all storage. Keep Android/user-data headroom. CACHE is rebuildable only; archive deletion requires confirmation. 200 GB is a target quota, not actual free space or RAM expansion.

## AI providers
Disclose context sent to remote provider, require opt-in, respect terms/rate limits, no quota evasion. Paid usage requires explicit approval. Redact keys/tokens. Show remote fallback clearly.

## MIRROR
Sandbox claims require threat-model testing. Separate directories and resource limits. Never auto-execute untrusted payloads.

## Bixby and permissions
No universal promise of Bixby-key remapping. Explain microphone, camera, location and accessibility permissions. Do not request permissions until feature use justifies them.

## Recovery
Validate backup format/version and file size. Import should merge safely by default, avoid overwriting, and preserve rollback.