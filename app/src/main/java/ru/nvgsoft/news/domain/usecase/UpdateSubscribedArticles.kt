package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticles @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(){
        repository.updateArticlesForAllSubscriptions()
    }
}