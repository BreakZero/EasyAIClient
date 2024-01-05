package org.easy.ai.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.easy.ai.model.AIModel

@Composable
internal fun ModelList(
    modifier: Modifier = Modifier,
    default: AIModel,
    models: List<AIModel>,
    onDismiss: () -> Unit,
    onSelected: (AIModel) -> Unit
) {
    var selectedItem: AIModel by remember {
        mutableStateOf(default)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSelected(selectedItem)
                onDismiss()
            }) {
                Text(text = "Confirm")
            }
        },
        title = {},
        text = {
            LazyColumn {
                items(models) { model ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem = model
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedItem == model,
                            onClick = null
                        )
                        Text(text = model.name)
                    }
                }
            }
        }
    )
}