package ru.nvgsoft.news

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.nvgsoft.news.presentation.startup.AppStartupManager
import javax.inject.Inject

@HiltAndroidApp
class NewsApp: Application(), Configuration.Provider {

    @Inject lateinit var  workerFactory: HiltWorkerFactory

    @Inject lateinit var appStartupManager: AppStartupManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        appStartupManager.startRefreshData()
    }

}