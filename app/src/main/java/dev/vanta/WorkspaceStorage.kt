package dev.vanta

import android.content.Context
import android.os.StatFs
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * INFINITE FABRIC's app-private workspace.
 *
 * The 200 GiB value is a logical ceiling, never a preallocated partition.
 * Android's actual free-space limit and a safety reserve are checked on writes.
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

data class WorkspaceEntry(
    val name: String,
    val relativePath: String,
    val isDirectory: Boolean,
    val sizeBytes: Long,
    val modifiedAtMillis: Long
)

data class WorkspaceResult(val success: Boolean, val message: String)

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

    fun list(context: Context, relativeDirectory: String = ""): List<WorkspaceEntry> {
        val directory = resolve(context, relativeDirectory) ?: return emptyList()
        if (!directory.exists() || !directory.isDirectory) return emptyList()
        return directory.listFiles().orEmpty()
            .filter { it.canonicalFile.toPath().startsWith(root(context).canonicalFile.toPath()) }
            .sortedWith(compareBy<File> { !it.isDirectory }.thenBy { it.name.lowercase() })
            .map { child ->
                val relative = root(context).canonicalFile.toPath()
                    .relativize(child.canonicalFile.toPath()).toString().replace(File.separatorChar, '/')
                WorkspaceEntry(child.name, relative, child.isDirectory, if (child.isFile) child.length() else 0L,
                    child.lastModified())
            }
    }

    fun createDirectory(context: Context, parentPath: String, name: String): WorkspaceResult {
        if (!isSafeName(name)) return WorkspaceResult(false, "Use a simple folder name without path separators.")
        val parent = resolve(context, parentPath) ?: return WorkspaceResult(false, "Invalid workspace path.")
        if (!parent.isDirectory) return WorkspaceResult(false, "The selected location is not a folder.")
        val target = File(parent, name)
        if (target.exists()) return WorkspaceResult(false, "An item with that name already exists.")
        return if (target.mkdir()) WorkspaceResult(true, "Folder created: $name")
        else WorkspaceResult(false, "Folder could not be created.")
    }

    fun createTextFile(context: Context, parentPath: String, name: String, contents: String): WorkspaceResult {
        if (!isSafeName(name) || name.endsWith(".")) return WorkspaceResult(false, "Use a valid file name.")
        val parent = resolve(context, parentPath) ?: return WorkspaceResult(false, "Invalid workspace path.")
        if (!parent.isDirectory) return WorkspaceResult(false, "The selected location is not a folder.")
        val target = File(parent, name)
        if (target.exists()) return WorkspaceResult(false, "An item with that name already exists.")
        val bytes = contents.toByteArray(StandardCharsets.UTF_8)
        val stats = StatFs(context.filesDir.absolutePath)
        val current = directorySize(root(context))
        if (!WorkspaceQuota.canAcceptWrite(current, bytes.size.toLong(), stats.availableBytes)) {
            return WorkspaceResult(false, "Write blocked by workspace quota or device free-space reserve.")
        }
        return try {
            val temporary = File(parent, ".$name.tmp")
            temporary.writeBytes(bytes)
            if (!temporary.renameTo(target)) {
                temporary.delete()
                WorkspaceResult(false, "File could not be finalized.")
            } else WorkspaceResult(true, "File created: $name")
        } catch (_: Exception) {
            WorkspaceResult(false, "File write failed. Check available device storage.")
        }
    }

    fun readTextFile(context: Context, relativePath: String): String? {
        val file = resolve(context, relativePath) ?: return null
        if (!file.isFile || file.length() > 2L * 1024L * 1024L) return null
        return try { file.readText(StandardCharsets.UTF_8) } catch (_: Exception) { null }
    }

    fun delete(context: Context, relativePath: String): WorkspaceResult {
        if (relativePath.isBlank()) return WorkspaceResult(false, "The workspace root cannot be deleted.")
        val target = resolve(context, relativePath) ?: return WorkspaceResult(false, "Invalid workspace path.")
        if (!target.exists()) return WorkspaceResult(false, "Item no longer exists.")
        return if (target.deleteRecursively()) WorkspaceResult(true, "Deleted: ${target.name}")
        else WorkspaceResult(false, "Could not delete this item.")
    }

    private fun resolve(context: Context, relativePath: String): File? {
        val base = root(context).canonicalFile
        val parts = relativePath.split('/').filter { it.isNotEmpty() }
        if (parts.any { !isSafeName(it) || it == "." || it == ".." }) return null
        val candidate = File(base, parts.joinToString(File.separator)).canonicalFile
        return if (candidate.toPath().startsWith(base.toPath())) candidate else null
    }

    private fun isSafeName(name: String): Boolean =
        name.isNotBlank() && name != "." && name != ".." &&
            name.none { it == '/' || it == '\\' || it == '\u0000' } &&
            name.length <= 120

    private fun directorySize(file: File): Long {
        if (!file.exists()) return 0L
        if (file.isFile) return file.length()
        return file.listFiles()?.sumOf(::directorySize) ?: 0L
    }
}
