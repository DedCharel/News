package ru.nvgsoft.news.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nvgsoft.news.domain.entity.Language
import ru.nvgsoft.news.domain.entity.Settings
import ru.nvgsoft.news.domain.repository.SettingsRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val intervalKey = intPreferencesKey("interval")
    private val notificationsEnabledKey = stringPreferencesKey("notifications_enabled")
    private val wifiOnlyKey = stringPreferencesKey("wifi_only")

    override fun getSettings(): Flow<Settings> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLanguage(language: Language) {
        TODO("Not yet implemented")
    }

    override suspend fun updateInterval(minute: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateWifiOnly(wifiOnly: Boolean) {
        TODO("Not yet implemented")
    }
}