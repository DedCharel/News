package ru.nvgsoft.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import ru.nvgsoft.news.domain.usecase.GetSettingsUseCase
import ru.nvgsoft.news.domain.usecase.UpdateSubscribedArticlesUseCase

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase,
    private val notificationsHelper: NotificationsHelper,
    private val getSettingsUseCase: GetSettingsUseCase
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "start")
        val settings = getSettingsUseCase().first()
        val updatedTopics = updateSubscribedArticlesUseCase()
        if (updatedTopics.isNotEmpty() && settings.notificationEnabled){
            notificationsHelper.showNewArticlesNotification(updatedTopics)            }
        Log.d("RefreshDataWorker", "finish")
        return Result.success()
    }
}