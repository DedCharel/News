package ru.nvgsoft.news.domain.usecase

import ru.nvgsoft.news.domain.entity.Interval
import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(interval: Interval){
        settingsRepository.updateInterval(interval.minutes)
    }
}