package org.easy.gemini.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.easy.gemini.feature.home.HomeContentRouter

const val HomeIndex = "home_index"
internal const val HomeRoute = "home_route"

fun NavGraphBuilder.homeScreen() {
    composable(HomeIndex) {
        HomeContentRouter()
    }
}