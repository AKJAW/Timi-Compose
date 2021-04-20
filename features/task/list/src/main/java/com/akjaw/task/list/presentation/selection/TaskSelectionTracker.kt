package com.akjaw.task.list.presentation.selection

import com.akjaw.task.api.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class TaskSelectionTracker(originalTaskFlow: Flow<List<Task>>) {

    private val selections = MutableStateFlow<List<Long>>(emptyList())
    val taskWithSelection: Flow<List<Task>> = combine(
        originalTaskFlow,
        selections,
        ::markSelectedTasks
    )

    fun toggleTask(task: Task) {
        if (task.isSelected) {
            selections.value = selections.value.filterNot { it == task.id }
        } else {
            selections.value = selections.value + task.id
        }
    }

    private fun markSelectedTasks(tasks: List<Task>, ids: List<Long>): List<Task> {
        return tasks.map { task ->
            task.copy(isSelected = ids.contains(task.id))
        }
    }
}
