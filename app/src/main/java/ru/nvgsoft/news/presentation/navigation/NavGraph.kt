package ru.nvgsoft.news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.nvgsoft.news.presentation.screen.settings.SettingsScreen
import ru.nvgsoft.news.presentation.screen.subscription.SubscriptionsScreen

@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Subscriptions.route
    ){
        composable(Screen.Subscriptions.route) {
            SubscriptionsScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Subscriptions: Screen("Subscriptions")

    data object Settings: Screen("Settings")
}