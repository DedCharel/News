package ru.nvgsoft.news.data.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nvgsoft.news.data.background.RefreshDataWorker
import ru.nvgsoft.news.data.local.ArticleDbModel
import ru.nvgsoft.news.data.local.NewsDao
import ru.nvgsoft.news.data.local.SubscriptionDbModel
import ru.nvgsoft.news.data.maper.toDBModels
import ru.nvgsoft.news.data.maper.toEntities
import ru.nvgsoft.news.data.remote.NewsApiService
import ru.nvgsoft.news.domain.entity.Article
import ru.nvgsoft.news.domain.entity.RefreshConfig
import ru.nvgsoft.news.domain.repository.NewsRepository
import java.util.concurrent.TimeUnit

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService,
    private val workManager: WorkManager
) : NewsRepository {


    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { subscriptions ->
            subscriptions.map { it.topic }
        }
    }

    override suspend fun addSubscriptions(topic: String) {
        newsDao.addSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String): Boolean{
       val articles = loadArticles(topic)
       val ids = newsDao.addArticles(articles)
        return ids.any{it != -1L}
    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel> {
        return try {
           newsApiService.loadArticles(topic).toDBModels(topic)
        } catch (e: Exception) {
            if (e is CancellationException){
                throw e
            }
            Log.e("NewsRepositoryImpl", e.stackTraceToString())
            listOf()
        }
    }

    override suspend fun removeSubscription(topic: String) {
        newsDao.deleteSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForAllSubscriptions(): List<String> {
        val updatedTopics = mutableListOf<String>()
        val subscriptions = newsDao.getAllSubscriptions().first()
        coroutineScope {
            subscriptions.forEach {
                launch {
                    val updated = updateArticlesForTopic(it.topic)
                    if (updated){
                        updatedTopics.add(it.topic)
                    }
                }
            }
        }
        return updatedTopics
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map {
            it.toEntities()
        }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }

    override fun startBackgroundRefresh(refreshConfig: RefreshConfig){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                if (refreshConfig.wifiOnly){
                    NetworkType.UNMETERED
                } else {
                    NetworkType.CONNECTED
                }
            )
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            refreshConfig.interval.minutes.toLong(), TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "Refresh data",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }
}