package ru.nvgsoft.news.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.entity.Interval
import ru.nvgsoft.news.domain.entity.Language
import ru.nvgsoft.news.domain.usecase.GetSettingsUseCase
import ru.nvgsoft.news.domain.usecase.UpdateIntervalUseCase
import ru.nvgsoft.news.domain.usecase.UpdateLanguageUseCase
import ru.nvgsoft.news.domain.usecase.UpdateNotificationEnabledUseCase
import ru.nvgsoft.news.domain.usecase.UpdateWifiOnlyUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateNotificationEnabledUseCase: UpdateNotificationEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial)
    val state = _state.asStateFlow()

    init {
        getSettingsUseCase()
            .onEach { settings ->
                SettingsState.Configuration(
                    language = settings.language,
                    interval = settings.interval,
                    wifiOnly = settings.wifiOnly,
                    notificationEnabled = settings.notificationEnabled
                )
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.SelectInterval -> {
                    updateIntervalUseCase(command.interval)
                }

                is SettingsCommand.SelectLanguage -> {
                    updateLanguageUseCase(command.language)
                }

                is SettingsCommand.SetNotificationsEnabled -> {
                    updateNotificationEnabledUseCase(command.notificationsEnabled)
                }

                is SettingsCommand.SetWifiOnly -> {
                    updateWifiOnlyUseCase(command.wifiOnly)
                }
            }
        }

    }
}

sealed interface SettingsCommand {
    data class SelectLanguage(val language: Language) : SettingsCommand

    data class SelectInterval(val interval: Interval) : SettingsCommand

    data class SetNotificationsEnabled(val notificationsEnabled: Boolean) : SettingsCommand

    data class SetWifiOnly(val wifiOnly: Boolean) : SettingsCommand
}

sealed interface SettingsState {
    companion object Initial : SettingsState

    data class Configuration(
        val language: Language,
        val interval: Interval,
        val wifiOnly: Boolean,
        val notificationEnabled: Boolean,
        val languages: List<Language> = Language.entries,
        val intervals: List<Interval> = Interval.entries
    ) : SettingsState
}