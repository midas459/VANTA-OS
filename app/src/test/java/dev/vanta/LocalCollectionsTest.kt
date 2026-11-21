package dev.vanta

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalCollectionsTest {
    @Test fun addRejectsBlankAndDuplicateIds() {
        val first = VantaTask("1", "Build core")
        assertEquals(listOf(first), LocalCollections.addTask(emptyList(), first))
        assertEquals(listOf(first), LocalCollections.addTask(listOf(first), first))
        assertEquals(emptyList<VantaTask>(), LocalCollections.addTask(emptyList(), first.copy(title = " ")))
    }

    @Test fun taskCanBeEditedCompletedAndDeleted() {
        val original = VantaTask("1", "Draft", createdAtEpochMillis = 1)
        val edited = LocalCollections.updateTask(listOf(original), "1", "Draft VANTA", 2)
        assertEquals("Draft VANTA", edited.single().title)
        assertEquals(2L, edited.single().updatedAtEpochMillis)
        val completed = LocalCollections.setTaskState(edited, "1", TaskState.COMPLETED, 3)
        assertEquals(TaskState.COMPLETED, completed.single().state)
        assertTrue(LocalCollections.deleteTask(completed, "1").isEmpty())
    }

    @Test fun blankTaskEditDoesNotDestroyExistingTitle() {
        val task = VantaTask("1", "Keep me")
        assertEquals(listOf(task), LocalCollections.updateTask(listOf(task), "1", "  ", 5))
    }

    @Test fun notesCanBeCreatedEditedSearchedAndDeleted() {
        val note = VantaNote("n1", "Physics", "Optics revision", 1)
        val notes = LocalCollections.addNote(emptyList(), note)
        assertEquals(listOf(note), LocalCollections.searchNotes(notes, "OPTICS"))
        val edited = LocalCollections.updateNote(notes, "n1", "Physics revision", "Lens formula", 2)
        assertEquals("Lens formula", edited.single().body)
        assertEquals(2L, edited.single().updatedAtEpochMillis)
        assertTrue(LocalCollections.searchNotes(edited, "missing").isEmpty())
        assertTrue(LocalCollections.deleteNote(edited, "n1").isEmpty())
    }

    @Test fun blankNoteAndDuplicateIdAreRejected() {
        val note = VantaNote("n1", "Title", "Body")
        assertEquals(emptyList<VantaNote>(), LocalCollections.addNote(emptyList(), note.copy(body = " ")))
        assertEquals(listOf(note), LocalCollections.addNote(listOf(note), note))
    }
}
