package ru.nvgsoft.news.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.nvgsoft.news.domain.repository.NewsRepository
import ru.nvgsoft.news.presentation.screen.subscription.SubscriptionsScreen
import ru.nvgsoft.news.presentation.ui.theme.NewsTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var repository: NewsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NewsTheme {
                SubscriptionsScreen(
                    onNavigateToSettings = {}
                )
            }
        }
    }
}

