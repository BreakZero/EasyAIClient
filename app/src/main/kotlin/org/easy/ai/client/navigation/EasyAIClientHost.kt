package org.easy.ai.client.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.easy.ai.chat.navigation.ChatEntryRoute
import org.easy.ai.chat.navigation.attachChat
import org.easy.ai.feature.settings.navigation.attachSettingsGraph
import org.easy.ai.feature.settings.navigation.entrySettings
import org.easy.ai.plugins.navigation.attachPluginRoutes
import org.easy.ai.plugins.navigation.navigateToPluginList

@Composable
fun EasyAIHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ChatEntryRoute) {
        attachChat(
            navigateToSettings = {
                navController.entrySettings()
            },
            navigateToPlugins = {
                navController.navigateToPluginList()
            }
        )
        attachSettingsGraph(navController)
        attachPluginRoutes(navController)
    }
}