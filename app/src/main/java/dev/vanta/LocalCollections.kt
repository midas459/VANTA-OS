package dev.vanta

/**
 * Local-first task and note domain models.
 * Pure Kotlin so core behavior can be unit-tested without an Android emulator.
 */
enum class TaskState { OPEN, COMPLETED }

data class VantaTask(
    val id: String,
    val title: String,
    val state: TaskState = TaskState.OPEN,
    val createdAtEpochMillis: Long = 0L,
    val updatedAtEpochMillis: Long = createdAtEpochMillis
)

data class VantaNote(
    val id: String,
    val title: String,
    val body: String,
    val createdAtEpochMillis: Long = 0L,
    val updatedAtEpochMillis: Long = createdAtEpochMillis
)

object LocalCollections {
    fun addTask(tasks: List<VantaTask>, task: VantaTask): List<VantaTask> =
        if (task.title.isBlank() || tasks.any { it.id == task.id }) tasks else listOf(task) + tasks

    fun updateTask(tasks: List<VantaTask>, id: String, title: String, now: Long): List<VantaTask> {
        if (title.isBlank()) return tasks
        return tasks.map { if (it.id == id) it.copy(title = title.trim(), updatedAtEpochMillis = now) else it }
    }

    fun setTaskState(tasks: List<VantaTask>, id: String, state: TaskState, now: Long): List<VantaTask> =
        tasks.map { if (it.id == id) it.copy(state = state, updatedAtEpochMillis = now) else it }

    fun deleteTask(tasks: List<VantaTask>, id: String): List<VantaTask> = tasks.filterNot { it.id == id }

    fun addNote(notes: List<VantaNote>, note: VantaNote): List<VantaNote> =
        if (note.title.isBlank() || note.body.isBlank() || notes.any { it.id == note.id }) notes else listOf(note) + notes

    fun updateNote(notes: List<VantaNote>, id: String, title: String, body: String, now: Long): List<VantaNote> {
        if (title.isBlank() || body.isBlank()) return notes
        return notes.map {
            if (it.id == id) it.copy(title = title.trim(), body = body.trim(), updatedAtEpochMillis = now) else it
        }
    }

    fun deleteNote(notes: List<VantaNote>, id: String): List<VantaNote> = notes.filterNot { it.id == id }

    fun searchNotes(notes: List<VantaNote>, query: String): List<VantaNote> {
        val needle = query.trim()
        if (needle.isEmpty()) return notes
        return notes.filter { it.title.contains(needle, ignoreCase = true) || it.body.contains(needle, ignoreCase = true) }
    }
}
