package dev.vanta

/**
 * A deliberately local, deterministic command parser.
 * It never launches apps, accesses files, or performs external actions.
 */
object DirectiveEngine {
    sealed interface Action {
        data object Help : Action
        data object Status : Action
        data class AddTask(val title: String) : Action
        data class AddNote(val text: String) : Action
        data class CompleteTask(val title: String) : Action
        data class DeleteTask(val title: String) : Action
        data class DeleteNote(val text: String) : Action
        data class SearchNotes(val query: String) : Action
        data object ListTasks : Action
        data class Unknown(val input: String) : Action
        data object Empty : Action
    }

    data class Result(val action: Action, val message: String)

    fun parse(raw: String): Result {
        val input = raw.trim()
        if (input.isEmpty()) return Result(Action.Empty, "Nothing to process. Enter a directive.")

        val command = input.substringBefore(' ').lowercase()
        val argument = input.substringAfter(' ', "").trim()

        return when (command) {
            "help" -> Result(
                Action.Help,
                "Commands: help · status · task <title> · note <text> · list · complete <title> · delete-task <title> · delete-note <title> · search-note <text>"
            )
            "status" -> Result(Action.Status, "VANTA core is local and responsive. No external actions are enabled.")
            "list" -> Result(Action.ListTasks, "Opening your local task list.")
            "complete" -> if (argument.isBlank()) Result(Action.Unknown(input), "Specify a task title to complete.")
                else Result(Action.CompleteTask(argument), "Task completion requested: $argument")
            "delete-task" -> if (argument.isBlank()) Result(Action.Unknown(input), "Specify a task title to delete.")
                else Result(Action.DeleteTask(argument), "Task deletion requested: $argument")
            "delete-note" -> if (argument.isBlank()) Result(Action.Unknown(input), "Specify a note title or text to delete.")
                else Result(Action.DeleteNote(argument), "Note deletion requested: $argument")
            "search-note" -> if (argument.isBlank()) Result(Action.Unknown(input), "Specify a search query.")
                else Result(Action.SearchNotes(argument), "Searching local notes: $argument")
            "task" -> if (argument.isBlank()) {
                Result(Action.Unknown(input), "A task needs a title. Try: task Prepare physics notes")
            } else {
                Result(Action.AddTask(argument), "Task captured locally: $argument")
            }
            "note" -> if (argument.isBlank()) {
                Result(Action.Unknown(input), "A note needs text. Try: note Remember the optics formula")
            } else {
                Result(Action.AddNote(argument), "Note saved locally.")
            }
            else -> Result(
                Action.Unknown(input),
                "Unknown directive. Nothing was executed. Type help to see supported commands."
            )
        }
    }
}
