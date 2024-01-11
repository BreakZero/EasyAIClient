package org.easy.ai.client.ui

import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import org.easy.ai.client.navigation.EasyAIHost
import org.easy.ai.system.components.EasyBackground

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EasyAIClientApp(
    windowSizeClass: WindowSizeClass,
    appState: EasyAIClientAppState = rememberAppState(windowSizeClass = windowSizeClass)
) {
    Surface(modifier = Modifier.semantics {
        testTagsAsResourceId = true
    }) {
        EasyBackground {
            EasyAIHost(appState = appState)
        }
    }
}