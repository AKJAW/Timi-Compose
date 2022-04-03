package com.akjaw.task.list.presentation

import com.akjaw.task.list.presentation.selection.TaskSelectionTrackerFactory
import com.akjaw.timi.kmp.core.shared.coroutines.TestDispatcherProvider
import com.akjaw.timi.kmp.feature.task.api.model.Task
import com.akjaw.timi.kmp.feature.task.api.model.TaskColor
import com.akjaw.timi.kmp.feature.task.dependency.composition.databaseModule
import com.akjaw.timi.kmp.feature.task.dependency.database.TaskEntityQueries
import com.akjaw.timi.kmp.feature.task.dependency.database.TimiDatabase
import com.akjaw.timi.kmp.feature.task.dependency.list.composition.taskListModule
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import strikt.api.expectThat
import strikt.assertions.isEqualTo

// TODO refactor?
@OptIn(ExperimentalCoroutinesApi::class)
internal class TaskListViewModelTest : KoinComponent {

    companion object {
        val TASK1 = Task(
            id = 0,
            name = "name",
            backgroundColor = TaskColor(22f, 22f, 22f),
            isSelected = false,
        )
        val TASK2 = Task(
            id = 1,
            name = "name2",
            backgroundColor = TaskColor(33f, 33f, 33f),
            isSelected = false,
        )
    }

    private val taskEntityQueries: TaskEntityQueries by inject()
    private val taskSelectionTrackerFactory = TaskSelectionTrackerFactory()
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()
    private lateinit var systemUnderTest: TaskListViewModel


    @BeforeEach
    fun setUp() {
        startKoin {
            modules(
                databaseModule,
                taskListModule,
                module {
                    single<SqlDriver> {
                        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                            TimiDatabase.Schema.create(this)
                        }
                    }
                }
            )
        }
        systemUnderTest = TaskListViewModel(
            getTasks = get(),
            deleteTasks = get(),
            addTask = get(),
            dispatcherProvider = TestDispatcherProvider(testCoroutineDispatcher),
            taskSelectionTrackerFactory = taskSelectionTrackerFactory,
        )
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Adding a task changes the list`(): Unit = runBlocking {
        givenTasks(TASK1)

        systemUnderTest.addTask(TASK2)

        val result = systemUnderTest.tasks.first()
        expectThat(result).isEqualTo(listOf(TASK1, TASK2))
    }

    @Test
    fun `Deleting tasks changes the list`(): Unit = runBlocking {
        givenTasks(TASK1, TASK2)

        systemUnderTest.deleteTasks(listOf(TASK1, TASK2))

        val result = systemUnderTest.tasks.first()
        expectThat(result).isEqualTo(emptyList())
    }

    private fun givenTasks(vararg task: Task) {
        task.forEach {
            taskEntityQueries.insertTask(
                id = it.id,
                position = 0,
                name = it.name,
                color = it.backgroundColor
            )
        }
    }
}
