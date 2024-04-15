package org.easy.ai.client.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.easy.ai.chat.navigation.ChatRoute
import org.easy.ai.chat.navigation.attachChat
import org.easy.ai.client.ui.EasyAIClientAppState
import org.easy.ai.feature.settings.navigation.navigateToSettings
import org.easy.ai.feature.settings.navigation.attachSettings
import org.easy.ai.plugins.navigation.attachPluginRoutes
import org.easy.ai.plugins.navigation.navigateToPlugin

@Composable
fun EasyAIHost(
    appState: EasyAIClientAppState,
    startDestination: String = ChatRoute
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        attachChat(
            navigateToSettings = {
                navController.navigateToSettings()
            },
            navigateToPlugins = {
                navController.navigateToPlugin()
            }
        )
        attachSettings()
        attachPluginRoutes(navController)
    }
}