package org.easy.ai.client.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.easy.ai.client.ui.EasyAIClientAppState
import org.easy.ai.feature.home.navigation.HomeIndex
import org.easy.ai.feature.home.navigation.homeScreen
import org.easy.ai.feature.settings.navigation.navigateToSettings
import org.easy.ai.feature.settings.navigation.settingsScreen

@Composable
fun EasyAIHost(
    appState: EasyAIClientAppState,
    startDestination: String = HomeIndex
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        homeScreen(
            navigateToSettings = {
                navController.navigateToSettings()
            }
        )
        settingsScreen()
    }
}