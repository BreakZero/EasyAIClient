package org.easy.gemini.feature.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ApiKeyEditorDialog(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        onDismissRequest = {}
    ) {
        Column {
            Text(text = "Enter your api key:")
            OutlinedTextField(value = "", onValueChange = {})
        }
    }
}