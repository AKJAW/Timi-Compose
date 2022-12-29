package com.akjaw.timi.kmp.feature.task.dependency.detail.composition

import com.akjaw.timi.kmp.feature.task.api.detail.presentation.TaskDetailViewModel
import com.akjaw.timi.kmp.feature.task.dependency.detail.domain.calendar.CalendarDaysCalculator
import com.akjaw.timi.kmp.feature.task.dependency.detail.presentation.CommonTaskDetailViewModel
import com.akjaw.timi.kmp.feature.task.dependency.detail.presentation.calendar.CalendarViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val taskDetailsModule = module {
    factoryOf(::CalendarDaysCalculator)
    factoryOf(::CalendarViewModel)
    factoryOf(::CommonTaskDetailViewModel) bind TaskDetailViewModel::class
}
