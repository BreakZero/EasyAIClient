package org.easy.ai.plugins.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.easy.ai.plugins.index.PluginListRoute
import org.easy.ai.plugins.multimodal.MultiModalRoute

@Serializable
internal data object PluginEntryRoute

@Serializable
internal data object PluginListRoute

@Serializable
internal data object GeminiModalRoute

fun NavController.navigateToPluginList(navOptions: NavOptions? = null) = navigate(PluginListRoute, navOptions)

fun NavController.navigateToMultiModal(navOptions: NavOptions? = null) = navigate(GeminiModalRoute, navOptions)

fun NavGraphBuilder.attachPluginRoutes(navController: NavController) {
    navigation<PluginEntryRoute>(startDestination = PluginListRoute) {
        composable<PluginListRoute> {
            PluginListRoute(
                popBack = navController::navigateUp,
                onPluginClick = {
                    navController.navigateToMultiModal()
                }
            )
        }
        composable<GeminiModalRoute> {
            MultiModalRoute(popBack = navController::popBackStack)
        }
    }
}