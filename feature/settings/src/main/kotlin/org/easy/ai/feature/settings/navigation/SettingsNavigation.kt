package org.easy.ai.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.easy.ai.feature.settings.SettingsRoute
import org.easy.ai.feature.settings.aimodel.AiModelManagerRoute

internal const val settingsEntryRoute = "settings_entry_route"
internal const val settingsIndexRoute = "settings_route"
internal const val aiModelManagerRoute = "ai_model_manager_route"

internal fun NavController.navigateToAiModels(navOptions: NavOptions? = null) {
    this.navigate(aiModelManagerRoute, navOptions)
}

fun NavController.entryToSettings(navOptions: NavOptions? = null) {
    this.navigate(settingsIndexRoute, navOptions)
}

fun NavGraphBuilder.attachSettingsGraph(navController: NavController) {
    navigation(route = settingsEntryRoute, startDestination = settingsIndexRoute) {
        composable(settingsIndexRoute) {
            SettingsRoute(
                popBack = navController::popBackStack,
                navigateToAiModels = navController::navigateToAiModels
            )
        }

        composable(aiModelManagerRoute) {
            AiModelManagerRoute(
                popBack = navController::popBackStack
            )
        }
    }
}