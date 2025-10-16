package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.updateNotificationEnabled(enabled)
    }
}