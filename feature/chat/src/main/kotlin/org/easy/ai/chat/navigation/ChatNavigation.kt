package org.easy.ai.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.easy.ai.chat.ChatRoute

@Serializable
object ChatEntryRoute

fun NavController.navigateToChat(navOptions: NavOptions? = null) =
    navigate(ChatEntryRoute, navOptions)

fun NavGraphBuilder.attachChat(navigateToPlugins: () -> Unit, navigateToSettings: () -> Unit) {
    composable<ChatEntryRoute> {
        ChatRoute(navigateToSettings = navigateToSettings, navigateToPlugins = navigateToPlugins)
    }
}