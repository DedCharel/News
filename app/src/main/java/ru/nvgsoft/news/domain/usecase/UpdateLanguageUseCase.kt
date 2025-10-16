package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.entity.Language
import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(language: Language){
        settingsRepository.updateLanguage(language)
    }
}