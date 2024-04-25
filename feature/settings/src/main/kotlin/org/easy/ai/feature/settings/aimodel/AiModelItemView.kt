package org.easy.ai.feature.settings.aimodel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.easy.ai.model.AiModel
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AiModelItem(
    modifier: Modifier = Modifier,
    aiModel: AiModel,
    onApiKeyChanged: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState()
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
                Text(text = aiModel.name)
                Checkbox(checked = true, onCheckedChange = {})
            }

            BasicTextField2(
                modifier = Modifier.fillMaxWidth(),
                state = textFieldState,
                decorator = @Composable {
                    val interactionSource = remember { MutableInteractionSource() }
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = textFieldState.text.toString(),
                        innerTextField = it,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        container = {
                            OutlinedTextFieldDefaults.ContainerBox(
                                enabled = true,
                                isError = false,
                                interactionSource = interactionSource,
                                colors = TextFieldDefaults.colors()
                            )
                        }
                    )
                }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AiModelItem_Preview() {
    EasyAITheme {
        AiModelItem(aiModel = AiModel.GEMINI) {}
    }
}