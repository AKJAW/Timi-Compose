package com.akjaw.timicompose

import android.app.Application
import com.akjaw.common.shared.kmmKoinModules
import com.akjaw.timi.android.core.composition.androidCoreModule
import com.akjaw.timi.android.feature.settings.ui.composition.settingsModule
import com.akjaw.timi.android.feature.task.detail.ui.composition.taskDetailsModule
import com.akjaw.timi.android.feature.task.list.ui.composition.taskUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class TimiComposeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TimiComposeApp)
            modules(allKoinModules)
        }
    }
}

val allKoinModules: List<Module> = listOf(
    *kmmKoinModules.toTypedArray(),
    androidCoreModule,
    taskUiModule,
    taskDetailsModule,
    settingsModule,
)
