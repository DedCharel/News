package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Inject

class RemoveSubscriptionsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topic: String){
        repository.removeSubscription(topic)
    }
}