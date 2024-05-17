package org.easy.ai.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.easy.ai.feature.settings.SettingsRoute
import org.easy.ai.feature.settings.aimodel.AiModelManagerRoute

@Serializable
internal data object SettingsEntryRoute

@Serializable
internal data object SettingsRoute

@Serializable
internal data object AiPlatformManagerRoute

internal fun NavController.navigateToAiModels(navOptions: NavOptions? = null) = navigate(AiPlatformManagerRoute, navOptions)

fun NavController.entrySettings(navOptions: NavOptions? = null) = navigate(SettingsRoute, navOptions)

fun NavGraphBuilder.attachSettingsGraph(navController: NavController) {
    navigation<SettingsEntryRoute>(startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsRoute(
                popBack = navController::popBackStack,
                navigateToAiModels = navController::navigateToAiModels
            )
        }
        composable<AiPlatformManagerRoute> {
            AiModelManagerRoute(
                popBack = navController::popBackStack
            )
        }
    }
}