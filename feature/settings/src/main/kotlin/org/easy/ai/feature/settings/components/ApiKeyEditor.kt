package org.easy.ai.feature.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun ApiKeyEditorDialog(
    modifier: Modifier = Modifier,
    apiKey: String,
    onDismiss: () -> Unit,
    applyApiKeyChanged: (String) -> Unit
) {
    var _apiKey by remember {
        mutableStateOf(apiKey)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                applyApiKeyChanged(_apiKey)
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
            }, onValueChange = { _apiKey = it })
        }
    )
}