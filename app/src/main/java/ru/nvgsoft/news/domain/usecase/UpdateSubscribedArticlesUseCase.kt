package ru.nvgsoft.news.domain.usecase

import kotlinx.coroutines.flow.first
import ru.nvgsoft.news.domain.repository.NewsRepository
import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): List<String> {
        val settings = settingsRepository.getSettings().first()
        return repository.updateArticlesForAllSubscriptions(settings.language)
    }
}