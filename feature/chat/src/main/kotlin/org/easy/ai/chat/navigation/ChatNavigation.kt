package org.easy.ai.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.easy.ai.chat.ChatRoute

const val ChatRoute = "chat_route"

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    this.navigate(ChatRoute, navOptions)
}

fun NavGraphBuilder.attachChat(navigateToPlugins: () -> Unit, navigateToSettings: () -> Unit) {
    composable(ChatRoute) {
        ChatRoute(navigateToSettings = navigateToSettings, navigateToPlugins = navigateToPlugins)
    }
}