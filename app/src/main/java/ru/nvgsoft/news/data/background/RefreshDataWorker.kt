package ru.nvgsoft.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.nvgsoft.news.domain.usecase.UpdateSubscribedArticlesUseCase

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "start")
        updateSubscribedArticlesUseCase()
        Log.d("RefreshDataWorker", "finish")
        return Result.success()
    }
}