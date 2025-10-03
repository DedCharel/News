package ru.nvgsoft.news.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.repository.NewsRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddSubscriptionsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topic: String){
        repository.addSubscriptions(topic)
        CoroutineScope(coroutineContext).launch {
            repository.updateArticlesForTopic(topic)
        }

    }
}