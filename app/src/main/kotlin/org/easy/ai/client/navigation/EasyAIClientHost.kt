package org.easy.ai.client.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.easy.ai.chat.navigation.ChatRoute
import org.easy.ai.chat.navigation.chatScreen
import org.easy.ai.chat.navigation.navigateToChat
import org.easy.ai.client.ui.EasyAIClientAppState
import org.easy.ai.feature.settings.navigation.navigateToSettings
import org.easy.ai.feature.settings.navigation.settingsScreen

@Composable
fun EasyAiHost(
    modifier: Modifier = Modifier,
    appState: EasyAIClientAppState,
    startDestination: String = MainRoute
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainRoute) {
            EasyAiMainRoute {
                navController.navigateToSettings()
            }
        }
        settingsScreen()
    }
}

@Composable
fun EasyAiMainHost(
    modifier: Modifier = Modifier,
    startDestination: String = ChatRoute
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        chatScreen { navController.navigateToChat() }
    }
}
