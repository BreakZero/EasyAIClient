package org.easy.ai.client.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.easy.ai.chat.navigation.ChatRoute
import org.easy.ai.chat.navigation.attachChat
import org.easy.ai.feature.settings.navigation.attachSettingsGraph
import org.easy.ai.feature.settings.navigation.entryToSettings
import org.easy.ai.plugins.navigation.attachPluginRoutes
import org.easy.ai.plugins.navigation.navigateToPlugin

@Composable
fun EasyAIHost(startDestination: String = ChatRoute) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        attachChat(
            navigateToSettings = {
                navController.entryToSettings()
            },
            navigateToPlugins = {
                navController.navigateToPlugin()
            }
        )
        attachSettingsGraph(navController)
        attachPluginRoutes(navController)
    }
}