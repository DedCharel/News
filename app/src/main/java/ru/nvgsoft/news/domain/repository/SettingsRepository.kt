package ru.nvgsoft.news.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.nvgsoft.news.domain.entity.Language
import ru.nvgsoft.news.domain.entity.Settings

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)

    suspend fun updateInterval(minute: Int)

    suspend fun updateNotificationEnabled(enabled: Boolean)

    suspend fun updateWifiOnly(wifiOnly: Boolean)
}
