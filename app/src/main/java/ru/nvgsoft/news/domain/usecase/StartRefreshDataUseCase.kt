package ru.nvgsoft.news.domain.usecase

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.nvgsoft.news.data.maper.toRefreshConfig
import ru.nvgsoft.news.domain.repository.NewsRepository
import ru.nvgsoft.news.domain.repository.SettingsRepository
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(){
        settingsRepository.getSettings()
            .map { it.toRefreshConfig() }
            .distinctUntilChanged()
            .onEach { newsRepository.startBackgroundRefresh(it) }
            .collect()

    }
}