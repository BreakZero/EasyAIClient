package org.easy.gemini.feature.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun ApiKeyEditorDialog(
    modifier: Modifier = Modifier,
    apiKey: String,
    onApiKeyChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    applyApiKeyChanged: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                applyApiKeyChanged()
                onDismiss()
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        properties = DialogProperties(),
        title = {},
        text = {
            OutlinedTextField(value = apiKey, label = {
                Text(text = "Enter your api key")
            }, onValueChange = onApiKeyChanged)
        }
    )
}