package org.easy.gemini.client

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import org.easy.gemini.client.ui.GeminiClientApp
import org.easy.gemini.system.ui.EasyGeminiTheme

@SuppressLint("ProduceStateDoesNotAssignValue")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyGeminiTheme(dynamicColor = true) {
                GeminiClientApp(windowSizeClass = calculateWindowSizeClass(activity = this))
            }
        }
    }
}
