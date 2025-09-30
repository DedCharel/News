@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.nvgsoft.news.presentation.screen.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.entity.Article
import ru.nvgsoft.news.domain.usecase.AddSubscriptionsUseCase
import ru.nvgsoft.news.domain.usecase.ClearAllArticlesUseCase
import ru.nvgsoft.news.domain.usecase.GetAllSubscriptionsUseCase
import ru.nvgsoft.news.domain.usecase.GetArticlesByTopicsUseCase
import ru.nvgsoft.news.domain.usecase.RemoveSubscriptionsUseCase
import ru.nvgsoft.news.domain.usecase.UpdateSubscribedArticlesUseCase
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val addSubscriptionsUseCase: AddSubscriptionsUseCase,
    private val clearAllArticlesUseCase: ClearAllArticlesUseCase,
    private val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    private val getArticlesByTopicsUseCase: GetArticlesByTopicsUseCase,
    private val removeSubscriptionsUseCase: RemoveSubscriptionsUseCase,
    private val updateSubscribedArticlesUseCase: UpdateSubscribedArticlesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SubscriptionState())
    val state = _state.asStateFlow()

    init {
        observeSubscriptions()
        observeSelectedTopic()
    }

    fun processCommand(command: SubscriptionCommand){
        when(command){
            SubscriptionCommand.ClearArticles -> {
                viewModelScope.launch {
                    val topics = state.value.selectedTopic
                    clearAllArticlesUseCase(topics)
                }
            }
            SubscriptionCommand.ClickSubscribe -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        val topic = state.value.query.trim()
                        addSubscriptionsUseCase(topic)
                        previousState.copy(query = "")
                    }
                }
            }
            is SubscriptionCommand.InputTopic -> {
                _state.update { previousState ->
                    previousState.copy(query = command.query)
                }
            }
            SubscriptionCommand.RefreshData -> {
                viewModelScope.launch {
                    updateSubscribedArticlesUseCase()
                }
            }
            is SubscriptionCommand.RemoveSubscription -> {
                viewModelScope.launch {
                    removeSubscriptionsUseCase(command.topic)
                }

            }
            is SubscriptionCommand.ToggleTopicSelection -> {
                _state.update { previousState ->
                    val newSubscriptions = previousState.subscriptions.toMutableMap()
                    val isSelected = newSubscriptions[command.topic] ?: false
                    newSubscriptions[command.topic] = !isSelected
                    previousState.copy(subscriptions = newSubscriptions)
                }
            }
        }
    }

    private fun observeSelectedTopic(){
        state.map { it.selectedTopic }
            .distinctUntilChanged()
            .flatMapLatest {
                getArticlesByTopicsUseCase(it)
            }
            .onEach {
                _state.update { previousState ->
                    previousState.copy( articles = it)
                }
            }.launchIn(viewModelScope)
    }

    private fun observeSubscriptions() {
        getAllSubscriptionsUseCase().onEach { subscriptions ->
            _state.update { previousState ->
                val updatedTopic = subscriptions.associateWith { topic ->
                    previousState.subscriptions[topic] ?: true
                }
                previousState.copy(subscriptions = updatedTopic)
            }
        }.launchIn(viewModelScope)
    }
}

sealed interface SubscriptionCommand {

    data class InputTopic(val query: String): SubscriptionCommand

    data object ClickSubscribe: SubscriptionCommand

    data object RefreshData: SubscriptionCommand

    data class ToggleTopicSelection(val topic: String): SubscriptionCommand

    data object ClearArticles: SubscriptionCommand

    data class RemoveSubscription(val topic: String): SubscriptionCommand
}

data class SubscriptionState(
    val query: String = "",
    val subscriptions: Map<String, Boolean> = mapOf(),
    val articles: List<Article> = listOf()
){
    val subscribeButtonEnabled: Boolean
        get() = query.isNotBlank()

    val selectedTopic: List<String>
        get() = subscriptions.filter { it.value }.map { it.key }
}