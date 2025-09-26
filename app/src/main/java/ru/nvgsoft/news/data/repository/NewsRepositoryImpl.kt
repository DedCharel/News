package ru.nvgsoft.news.data.repository

import android.util.Log
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nvgsoft.news.data.local.ArticleDbModel
import ru.nvgsoft.news.data.local.NewsDao
import ru.nvgsoft.news.data.local.SubscriptionDbModel
import ru.nvgsoft.news.data.maper.toDBModels
import ru.nvgsoft.news.data.maper.toEntities
import ru.nvgsoft.news.data.remote.NewsApiService
import ru.nvgsoft.news.domain.entity.Article
import ru.nvgsoft.news.domain.repository.NewsRepository

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService
) : NewsRepository {
    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { subscriptions ->
            subscriptions.map { it.topic }
        }
    }

    override suspend fun addSubscriptions(topic: String) {
        newsDao.addSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String) {
       val articles = loadArticles(topic)
        newsDao.addArticles(articles)

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

    override suspend fun updateArticlesForAllSubscriptions() {
        val subscriptions = newsDao.getAllSubscriptions().first()
        coroutineScope {
            subscriptions.forEach {
                launch {
                    updateArticlesForTopic(it.topic)
                }
            }
        }
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map {
            it.toEntities()
        }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }
}