package org.easy.ai.plugins.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.easy.ai.plugins.index.PluginListRoute
import org.easy.ai.plugins.multimodal.MultiModalRoute

internal const val pluginEntryRoute = "plugin_entry_route"
internal const val pluginListRoute = "plugin_list_route"
internal const val multiModalRoute = "_multimodal_route"

fun NavController.navigateToPlugin(navOptions: NavOptions? = null) {
    this.navigate(pluginEntryRoute, navOptions)
}

fun NavController.navigateToMultiModal(navOptions: NavOptions? = null) {
    this.navigate(multiModalRoute, navOptions)
}

fun NavGraphBuilder.attachPluginRoutes(
    navController: NavController
) {
    navigation(route = pluginEntryRoute, startDestination = pluginListRoute) {
        composable(pluginListRoute) {
            PluginListRoute(
                popBack = navController::navigateUp,
                onPluginClick = {
                    navController.navigateToMultiModal()
                }
            )
        }
        composable(multiModalRoute) {
            MultiModalRoute(popBack = navController::navigateUp)
        }
    }
}