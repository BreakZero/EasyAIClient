package org.easy.ai.feature.settings.aimodel

import androidx.compose.runtime.Stable
import org.easy.ai.model.AiModel

@Stable
internal data class AiModelUiModel(
    val aiModel: AiModel,
    val apiKey: String
)

internal data class AiModelUiState(
    val inEditModel: Boolean = false,
    val selectedAi: AiModel = AiModel.GEMINI,
    val selectedKey: String = ""
)

sealed interface AiModelUiEvent {
    data class ClickEdit(
        val aiModel: AiModel,
        val initKey: String
    ) : AiModelUiEvent

    data object EditDone : AiModelUiEvent
    data class UpdateApiKey(val aiModel: AiModel, val apiKey: String) : AiModelUiEvent
}