package org.easy.gemini.client.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.easy.gemini.client.ui.GeminiClientAppState
import org.easy.gemini.feature.home.navigation.HomeIndex
import org.easy.gemini.feature.home.navigation.homeScreen

@Composable
fun GeminiHost(
    geminiClientAppState: GeminiClientAppState,
    startDestination: String = HomeIndex
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        homeScreen()
    }
}