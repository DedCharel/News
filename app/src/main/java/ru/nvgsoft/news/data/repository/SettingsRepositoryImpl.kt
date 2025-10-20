package ru.nvgsoft.news.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nvgsoft.news.data.maper.toInterval
import ru.nvgsoft.news.domain.entity.Language
import ru.nvgsoft.news.domain.entity.Settings
import ru.nvgsoft.news.domain.repository.SettingsRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val intervalKey = intPreferencesKey("interval")
    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")
    private val wifiOnlyKey = booleanPreferencesKey("wifi_only")

    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            val languageAsSetting = preferences[languageKey] ?: Settings.DEFAULT_LANGUAGE.name
            val language = Language.valueOf(languageAsSetting)
            val interval = preferences[intervalKey]?.toInterval() ?: Settings.DEFAULT_INTERVAL
            val notificationEnabled = preferences[notificationsEnabledKey] ?: Settings.DEFAULT_NOTIFICATION_ENABLED
            val wifiOnly = preferences[wifiOnlyKey] ?: Settings.DEFAULT_WIFI_ONLY
            Settings(
                language =language,
                interval = interval,
                notificationEnabled =notificationEnabled,
                wifiOnly = wifiOnly
            )

        }
    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language.name
        }
    }

    override suspend fun updateInterval(minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[intervalKey] = minute
        }
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = enabled
        }
    }

    override suspend fun updateWifiOnly(wifiOnly: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[wifiOnlyKey] = wifiOnly
        }
    }
}