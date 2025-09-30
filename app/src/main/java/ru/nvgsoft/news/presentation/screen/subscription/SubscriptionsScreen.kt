package ru.nvgsoft.news.presentation.screen.subscription

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.nvgsoft.news.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value
    Scaffold(
        modifier = modifier,
        topBar = {
            SubscriptionsTopBar(
                modifier = modifier,
                onRefreshData = {
                    viewModel.processCommand(SubscriptionCommand.RefreshData)
                },
                onClearArticlesClick = {
                    viewModel.processCommand(SubscriptionCommand.ClearArticles)
                },
                onSettingsClick = { onNavigateToSettings() }
            )
        }
    ) { innerPadding ->
        Subscriptions(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            subscriptions = currentState.subscriptions,
            query = currentState.query,
            isSubscribeButtonEnabled = currentState.query.isNotBlank(),
            onQueryChange = {
                viewModel.processCommand(SubscriptionCommand.InputTopic(it))
            },
            onTopicClick = {
                viewModel.processCommand(SubscriptionCommand.ToggleTopicSelection(it))
            },
            onDeleteSubscription = {
                viewModel.processCommand(SubscriptionCommand.RemoveSubscription(it))
            },
            onSubscribeButtonClick = {
                viewModel.processCommand(SubscriptionCommand.ClickSubscribe)
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsTopBar(
    modifier: Modifier = Modifier,
    onRefreshData: () -> Unit,
    onClearArticlesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.subscriptions_title)) },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onRefreshData() }
                    .padding(end = 8.dp),
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.update_articles)
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClearArticlesClick() }
                    .padding(end = 8.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.clear_articles)
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onSettingsClick() }
                    .padding(end = 16.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings)
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .clickable {

                    },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )

        }
    )
}

@Composable
fun SubscriptionChip(
    modifier: Modifier = Modifier,
    topic: String,
    isSelected: Boolean,
    onSubscriptionClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = {
            onSubscriptionClick(topic)
        },
        trailingIcon = {

            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onDeleteSubscription(topic) },
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.remove_subscription)
            )
        },
        label = {Text(topic)}
    )
}

@Composable
fun Subscriptions(
    modifier: Modifier = Modifier,
    subscriptions: Map<String, Boolean>,
    query: String,
    isSubscribeButtonEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onSubscribeButtonClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit

){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChange,
            label = {
                Text(stringResource(R.string.what_interests_you))
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isSubscribeButtonEnabled,
            onClick = {
               onSubscribeButtonClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_subscription)
            )

            Spacer(Modifier.width(8.dp))

            Text(stringResource(R.string.add_subscriptions_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        if(subscriptions.isNotEmpty()){
            Text(
                text = stringResource(R.string.subscriptions_lable, subscriptions.size),
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                subscriptions.forEach { topic, isSelected ->
                    item (key = topic) {
                        SubscriptionChip(
                            topic = topic,
                            isSelected = isSelected,
                            onSubscriptionClick = onTopicClick,
                            onDeleteSubscription = onDeleteSubscription
                        )
                    }
                }

            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.no_subscriptions),
                textAlign = TextAlign.Center
            )
        }

    }
}



