package org.easy.gemini.client.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): GeminiClientAppState {
    return remember(windowSizeClass, coroutineScope, navController) {
        GeminiClientAppState(navController, coroutineScope, windowSizeClass)
    }
}

@Stable
class GeminiClientAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    private val windowSizeClass: WindowSizeClass
)