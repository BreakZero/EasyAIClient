package org.easy.ai.client.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
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
    EasyBackground(
        modifier = Modifier
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )
            ) {
                EasyAIHost()
            }

        }
    }
}