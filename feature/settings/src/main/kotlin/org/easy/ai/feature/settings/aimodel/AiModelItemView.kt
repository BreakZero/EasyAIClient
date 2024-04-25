package org.easy.ai.feature.settings.aimodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEditOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.easy.ai.model.AiModel
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme

@Composable
internal fun AiModelItem(
    modifier: Modifier = Modifier,
    aiModelInfo: AiModelUiModel,
    onEdit: (AiModelUiModel) -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.titleMedium, text = aiModelInfo.aiModel.name)
                IconButton(onClick = {
                    onEdit(aiModelInfo)
                }) {
                    Icon(imageVector = Icons.Default.ModeEditOutline, contentDescription = null)
                }
            }

            Text(style = MaterialTheme.typography.labelMedium, text = "ApiKey: ")
            Text(style = MaterialTheme.typography.bodyMedium, text = aiModelInfo.apiKey)
        }
    }
}

@ThemePreviews
@Composable
private fun AiModelItem_Preview() {
    EasyAITheme {
        AiModelItem(
            aiModelInfo = AiModelUiModel(
                aiModel = AiModel.GEMINI,
                isActivated = true,
                apiKey = "quehwoiqjgefjadskfj"
            )
        ) {}
    }
}