package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateWifiOnlyUswCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(wifiOnly: Boolean) {
        settingsRepository.updateWifiOnly(wifiOnly)
    }
}