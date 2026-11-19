# INFINITE FABRIC — 200 GB Workspace Specification

## Intent
User wants a dedicated workspace experience targeting 200 GB. This is a storage/workspace goal, not RAM expansion, partitioning, or pre-reserving a fixed block of space.

## Capacity model
Show separately:
1. Target workspace quota (configured; default 200 GB).
2. Actual directory content size (measured).
3. Volume total capacity.
4. Volume available/free bytes.
5. Estimated app-private usage, if Android exposes it.
6. Physical RAM and current system/app memory estimates.
7. Cache and durable files separately.

Never show 200 GB as available unless the chosen volume actually supports that amount and free capacity is sufficient. A logical quota may exceed actual free space but must be clearly marked as a target and never permit writes beyond capacity.

## User-controlled volume
Use Android Storage Access Framework folder selection. Explain why folder access is needed. Persist URI permission if supported. User can revoke permission; detect and recover. Do not silently scan unrelated folders.

## Layout
- ACTIVE: current mission outputs and active files.
- CACHE: regenerable data; limited quota.
- VAULT: durable knowledge and user-approved context.
- ARCHIVE: inactive work, not automatically deleted.
- MIRROR: virtual-environment assets and snapshots.

## Write safety
- Check free space before large writes and during long operations where possible.
- Maintain configurable reserve margin.
- Use temp file then atomic rename where supported.
- Avoid interrupted/corrupt writes.
- Track manifest, timestamps, hashes for integrity.
- Offer cleanup candidates, but ask before deleting.
- Automatic cleanup may only touch explicitly identified rebuildable cache with user-configured policy.
- No deleting archive/user files automatically.

## RAM and flash distinction
- RAM is volatile physical memory used by active processes.
- Flash/storage holds durable files.
- Cache can reduce repeated work but consumes storage.
- Android memory pressure may terminate processes.
- Storage-backed paging is slower and increases write wear; it is not a promise of RAM expansion and should not be enabled as a magical boost.
- VANTA should optimize memory use and gracefully release cache instead.

## Tests
- SAF grant and revocation.
- External volume unplugged.
- Low space and full volume.
- Permission loss.
- Interrupted write.
- Quota exceeded.
- Large directory scan without UI freeze.
- Symlink/unsupported files and provider errors.
- Export/import consistency.
- Recovery from partial manifests.