package dev.vanta

import android.content.Context
import android.os.StatFs
import java.io.File

/**
 * INFINITE FABRIC's first safe storage layer.
 *
 * The 200 GiB value is a logical ceiling, not a preallocated partition.
 * Files remain inside Android's app-private sandbox and are deleted if the
 * user uninstalls the app unless exported/backed up separately.
 */
data class WorkspaceSnapshot(
    val targetBytes: Long,
    val usedBytes: Long,
    val freeBytesOnInternalVolume: Long,
    val totalBytesOnInternalVolume: Long,
    val reserveBytes: Long
) {
    val remainingQuotaBytes: Long get() = (targetBytes - usedBytes).coerceAtLeast(0)
    val usableFreeBytes: Long get() = (freeBytesOnInternalVolume - reserveBytes).coerceAtLeast(0)
    val quotaPercent: Int
        get() = if (targetBytes <= 0) 0 else ((usedBytes.toDouble() / targetBytes) * 100)
            .toInt().coerceIn(0, 100)
}

object WorkspaceQuota {
    const val GIB: Long = 1024L * 1024L * 1024L
    const val TARGET_BYTES: Long = 200L * GIB
    const val RESERVE_BYTES: Long = 2L * GIB

    fun canAcceptWrite(
        currentUsageBytes: Long,
        requestedBytes: Long,
        actualFreeBytes: Long,
        reserveBytes: Long = RESERVE_BYTES
    ): Boolean {
        if (currentUsageBytes < 0 || requestedBytes < 0 || actualFreeBytes < 0 || reserveBytes < 0) return false
        if (currentUsageBytes > TARGET_BYTES) return false
        if (requestedBytes > TARGET_BYTES - currentUsageBytes) return false
        return requestedBytes <= (actualFreeBytes - reserveBytes).coerceAtLeast(0)
    }
}

object WorkspaceStorage {
    private const val DIRECTORY_NAME = "vanta-fabric"

    fun root(context: Context): File =
        File(context.filesDir, DIRECTORY_NAME).apply { mkdirs() }

    fun snapshot(context: Context): WorkspaceSnapshot {
        val root = root(context)
        val stats = StatFs(context.filesDir.absolutePath)
        return WorkspaceSnapshot(
            targetBytes = WorkspaceQuota.TARGET_BYTES,
            usedBytes = directorySize(root),
            freeBytesOnInternalVolume = stats.availableBytes,
            totalBytesOnInternalVolume = stats.totalBytes,
            reserveBytes = WorkspaceQuota.RESERVE_BYTES
        )
    }

    private fun directorySize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        return file.listFiles()?.sumOf(::directorySize) ?: 0L
    }
}
