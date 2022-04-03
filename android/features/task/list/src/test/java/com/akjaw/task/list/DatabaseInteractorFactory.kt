package com.akjaw.task.list

import com.akjaw.task.TaskEntityQueries
import com.akjaw.task.list.data.AddTaskToDatabase
import com.akjaw.task.list.data.DeleteTasksFromDatabase
import com.akjaw.task.list.data.GetTasksFromDatabase
import com.akjaw.timi.kmp.feature.task.api.AddTask
import com.akjaw.timi.kmp.feature.task.api.DeleteTasks
import com.akjaw.timi.kmp.feature.task.api.GetTasks

class DatabaseInteractorFactory(private val queries: TaskEntityQueries) {

    fun createGetTasks(): GetTasks = GetTasksFromDatabase(queries)

    fun createAddTask(): AddTask = AddTaskToDatabase(queries)

    fun createDeleteTasks(): DeleteTasks = DeleteTasksFromDatabase(queries)
}
