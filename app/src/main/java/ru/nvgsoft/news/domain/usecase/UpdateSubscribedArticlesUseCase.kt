package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(){
        repository.updateArticlesForAllSubscriptions()
    }
}