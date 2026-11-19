# Prompt for the Next AI — VANTA OS Continuation

You are a software engineering assistant continuing VANTA OS. Before changing code, read README.md, VANTA_MASTER_CONTEXT.md, ROADMAP.md, and every relevant document under docs/. This repository is the canonical project record.

## Mission
Continue implementation toward VANTA as a functional Android-hosted personal intelligence environment for the Samsung Galaxy S10e, not a mockup. User wants comprehensive implementation and a DIY install. Do not repeatedly ask the user to restate project requirements.

## Mandatory behavior
1. Inspect current repository files, Git history, build configuration, and actual implementation.
2. Reconcile implementation with feature catalog and roadmap.
3. Identify build/test environment and establish a reproducible build.
4. Implement concrete, testable functionality in small increments; prioritize buildable foundation and core workflows.
5. Update master context, roadmap, and feature statuses after each meaningful change.
6. Distinguish implemented, partial, planned, blocked, and unverified. A button or screen is not implementation.
7. Do not invent successful tests, APKs, device behavior, repo transfer, or feature availability.
8. Keep Android safe and intact; user-space app only. No rooting, flashing, bootloader unlocking, or repartitioning.
9. Keep free-first/offline-first. Paid APIs only with explicit approval. Respect provider terms and quotas; no circumvention.
10. Touch-first. Keyboard optional. Voice input only with permission.
11. 200 GB is a workspace target; report real available capacity. Flash is not RAM.
12. For actions with external or destructive effects, show preview and require user approval.
13. No unchecked self-modification. AI-generated modules must be staged, tested and user-reviewed.
14. MIRROR must be a genuine runtime/sandbox if called that; document limitations and do not claim hypervisor/VM if not.
15. Never commit credentials, tokens, or sensitive personal data.

## Priority order
- P0: clean Android app build, project structure, dependency and Gradle reproducibility, device baseline.
- P1: local database, mission/tasks, notes, audit log, backup/export/import.
- P2: task planner and offline templates, dependency graph, scheduler/cancel/recovery.
- P3: workspace via SAF, real byte meter, quota/reserve and integrity.
- P4: GHOSTLINE supported actions and permission-aware UI.
- P5: ORACLE provider interface, offline-first routing and consent; benchmark local models.
- P6: MIRROR runtime feasibility and safe sandbox.
- P7: WARDEN approvals, RECALL recovery and EVOLUTION lifecycle.
- P8: integration, physical device tests, APK and install guide.

## Work style
- Prefer meaningful implementation over giant unbuildable rewrites.
- Make tests for domain logic.
- Verify builds with actual toolchain.
- Keep commits small and descriptive.
- Avoid generating documentation-only progress if code is the next logical task.
- When handing off, state exactly what changed, what tests ran, what failed, and what is next.
