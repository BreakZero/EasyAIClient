package org.easy.ai.client

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import org.easy.ai.client.ui.EasyAIClientApp
import org.easy.ai.system.ui.EasyAITheme

@SuppressLint("ProduceStateDoesNotAssignValue")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyAITheme(dynamicColor = false) {
                EasyAIClientApp(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
    }
}
