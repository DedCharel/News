package ru.nvgsoft.news.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.nvgsoft.news.domain.entity.Article
import ru.nvgsoft.news.domain.entity.RefreshConfig

interface NewsRepository {

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun addSubscriptions(topic: String)

    suspend fun updateArticlesForTopic(topic: String): Boolean

    suspend fun removeSubscription(topic: String)

    fun startBackgroundRefresh(refreshConfig: RefreshConfig)

    suspend fun updateArticlesForAllSubscriptions(): List<String>

    fun getArticlesByTopics(topics: List<String>): Flow<List<Article>>

    suspend fun clearAllArticles(topics: List<String>)
}