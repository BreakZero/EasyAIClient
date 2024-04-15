package org.easy.ai.feature.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.DialogProperties
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ApiKeyEditorDialog(
    modifier: Modifier = Modifier,
    initialKey: String,
    onDismiss: () -> Unit,
    applyApiKeyChanged: (String) -> Unit
) {
    val apiKeyTextField = rememberTextFieldState(initialKey)
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                applyApiKeyChanged(apiKeyTextField.text.toString())
                onDismiss()
            }, enabled = apiKeyTextField.text.isNotBlank()) {
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
            BasicTextField2(
                modifier = Modifier.fillMaxWidth(),
                state = apiKeyTextField,
                decorator = @Composable {
                    val interactionSource = remember { MutableInteractionSource() }
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = apiKeyTextField.text.toString(),
                        innerTextField = it,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        label = {
                            Text(text = "ENTER YOUR API KEY")
                        }
                    )
                }
            )
        }
    )
}

@ThemePreviews
@Composable
private fun ApiEditor_Preview() {
    EasyAITheme {
        ApiKeyEditorDialog(initialKey = "", onDismiss = { /*TODO*/ }) {

        }
    }
}