package org.easy.gemini.client.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import org.easy.gemini.client.navigation.GeminiHost
import org.easy.gemini.system.components.EasyBackground

@Composable
fun GeminiClientApp(
    windowSizeClass: WindowSizeClass,
    appState: GeminiClientAppState = rememberAppState(windowSizeClass = windowSizeClass)
) {
    EasyBackground {
        GeminiHost(geminiClientAppState = appState)
    }
}