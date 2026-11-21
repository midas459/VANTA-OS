package dev.vanta

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DirectiveEngineTest {
    @Test fun parsesHelpCaseInsensitively() {
        assertTrue(DirectiveEngine.parse(" HELP ").action is DirectiveEngine.Action.Help)
    }

    @Test fun parsesTaskAndPreservesTitle() {
        val result = DirectiveEngine.parse("task Finish VANTA")
        assertEquals(DirectiveEngine.Action.AddTask("Finish VANTA"), result.action)
    }

    @Test fun parsesNoteAndPreservesText() {
        val result = DirectiveEngine.parse("note Keep this on device")
        assertEquals(DirectiveEngine.Action.AddNote("Keep this on device"), result.action)
    }

    @Test fun rejectsEmptyTask() {
        assertTrue(DirectiveEngine.parse("task   ").action is DirectiveEngine.Action.Unknown)
    }

    @Test fun unknownCommandsNeverBecomeActions() {
        assertTrue(DirectiveEngine.parse("delete everything").action is DirectiveEngine.Action.Unknown)
    }

    @Test fun parsesTaskLifecycleAndNoteSearchCommands() {
        assertEquals(DirectiveEngine.Action.CompleteTask("Study physics"),
            DirectiveEngine.parse("complete Study physics").action)
        assertEquals(DirectiveEngine.Action.DeleteTask("Study physics"),
            DirectiveEngine.parse("delete-task Study physics").action)
        assertEquals(DirectiveEngine.Action.DeleteNote("Optics"),
            DirectiveEngine.parse("delete-note Optics").action)
        assertEquals(DirectiveEngine.Action.SearchNotes("lens"),
            DirectiveEngine.parse("search-note lens").action)
    }

    @Test fun lifecycleCommandsRejectMissingArguments() {
        assertTrue(DirectiveEngine.parse("complete").action is DirectiveEngine.Action.Unknown)
        assertTrue(DirectiveEngine.parse("delete-task").action is DirectiveEngine.Action.Unknown)
        assertTrue(DirectiveEngine.parse("delete-note").action is DirectiveEngine.Action.Unknown)
        assertTrue(DirectiveEngine.parse("search-note").action is DirectiveEngine.Action.Unknown)
    }

    @Test fun blankInputIsSafe() {
        assertTrue(DirectiveEngine.parse("   ").action is DirectiveEngine.Action.Empty)
    }
}
