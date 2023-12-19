package org.easy.gemini.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsRoute() {
    SettingsScreen()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SettingsScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { _ ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }
}