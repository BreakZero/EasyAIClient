package org.easy.ai.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.easy.ai.model.AiModel
import org.easy.ai.system.ui.R
import org.easy.ai.system.ui.localDim

@Composable
internal fun AiModelListDialog(
    modifier: Modifier = Modifier,
    default: AiModel? = null,
    onDismiss: () -> Unit,
    onSelected: (AiModel) -> Unit
) {
    var selectedItem: AiModel by remember {
        mutableStateOf(default ?: AiModel.GEMINI)
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
        title = { Text(text = "Select Ai For Chat") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceSmall)
            ) {
                items(AiModel.entries) { model ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                if (model == AiModel.CHAT_GPT) return@clickable
                                selectedItem = model
                            }
                            .padding(MaterialTheme.localDim.spaceSmall),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.localDim.spaceSmall
                        )
                    ) {
                        RadioButton(
                            selected = selectedItem == model,
                            onClick = null,
                            enabled = model == AiModel.GEMINI
                        )
                        Column {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = model.name
                            )
                            if (model == AiModel.CHAT_GPT) {
                                Text(
                                    style = MaterialTheme.typography.labelSmall,
                                    text = stringResource(
                                        id = R.string.core_system_ui_ai_model_not_supported_yet
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}