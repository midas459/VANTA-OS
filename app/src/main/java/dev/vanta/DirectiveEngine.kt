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
                "Commands: help · status · task <title> · note <text> · list"
            )
            "status" -> Result(Action.Status, "VANTA core is local and responsive. No external actions are enabled.")
            "list" -> Result(Action.ListTasks, "Opening your local task list.")
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
