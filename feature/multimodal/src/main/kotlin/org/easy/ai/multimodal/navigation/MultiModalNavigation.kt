package org.easy.ai.multimodal.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.easy.ai.multimodal.MultiModalRoute

internal const val multiModalRoute = "_multimodal_route"

fun NavController.navigateToMultiModal(navOptions: NavOptions? = null) {
    this.navigate(multiModalRoute, navOptions)
}

fun NavGraphBuilder.multiModalScreen(
    popBack: () -> Unit
) {
    composable(multiModalRoute) {
        MultiModalRoute(popBack = popBack)
    }
}