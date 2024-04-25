package org.easy.ai.feature.settings

import androidx.compose.runtime.Stable
import org.easy.ai.model.AiModel

@Stable
data class SettingsUiState(
    val aiModel: AiModel = AiModel.GEMINI,
    val apiKey: String = "",
    val model: AiModel = AiModel.GEMINI,
    val isAutomaticSaveChat: Boolean = false,
    val isApiKeyEditorShowed: Boolean = false,
    val isModelListShowed: Boolean = false
)

sealed interface SettingsEvent {
    data object ShowApiKeyEditor : SettingsEvent
    data object HideApiKeyEditor : SettingsEvent
    data object ShowModelList : SettingsEvent
    data object HideModelList : SettingsEvent
    data class AutomaticSaveChatChanged(val isChecked: Boolean): SettingsEvent
    data class SavedModel(val model: AiModel) : SettingsEvent
    data class SavedApiKey(val apiKey: String) : SettingsEvent
}
