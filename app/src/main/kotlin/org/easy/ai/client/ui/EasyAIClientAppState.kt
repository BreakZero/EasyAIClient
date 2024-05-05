package org.easy.ai.client.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
): EasyAIClientAppState {
    return remember(windowSizeClass, coroutineScope, navController) {
        EasyAIClientAppState(navController, coroutineScope, windowSizeClass)
    }
}

@Stable
class EasyAIClientAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    private val windowSizeClass: WindowSizeClass
) {
    val shouldShowNavRail: Boolean
        get() = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
}