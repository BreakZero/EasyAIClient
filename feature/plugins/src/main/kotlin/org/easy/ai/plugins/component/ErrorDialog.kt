package org.easy.ai.plugins.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ErrorDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
        title = {
            Text(style = MaterialTheme.typography.titleMedium, text = title)
        },
        text = {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }
    )
}