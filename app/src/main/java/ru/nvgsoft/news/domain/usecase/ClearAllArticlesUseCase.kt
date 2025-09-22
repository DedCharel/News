package ru.nvgsoft.news.domain.usecase

import jakarta.inject.Inject
import ru.nvgsoft.news.domain.repository.NewsRepository

class ClearAllArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
){
    suspend operator fun invoke(topics: List<String>) {
        repository.clearAllArticles(topics)
    }
}