package org.easy.ai.client.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import org.easy.ai.client.navigation.EasyAIHost
import org.easy.ai.system.components.EasyBackground

@Composable
fun EasyAIClientApp(
    windowSizeClass: WindowSizeClass,
    appState: EasyAIClientAppState = rememberAppState(windowSizeClass = windowSizeClass)
) {
    EasyBackground {
        EasyAIHost(appState = appState)
    }
}