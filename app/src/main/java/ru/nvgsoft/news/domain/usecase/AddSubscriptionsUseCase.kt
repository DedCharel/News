package ru.nvgsoft.news.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.repository.NewsRepository
import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddSubscriptionsUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(topic: String){
        repository.addSubscriptions(topic)
        CoroutineScope(coroutineContext).launch {
            val settings = settingsRepository.getSettings().first()
            repository.updateArticlesForTopic(topic, settings.language)
        }

    }
}