package org.easy.ai.feature.settings

import androidx.compose.runtime.Stable
import org.easy.ai.model.AIModel
import org.easy.ai.model.ModelPlatform

@Stable
data class SettingsUiState(
    val modelPlatform: ModelPlatform = ModelPlatform.GEMINI,
    val apiKey: String = "",
    val model: AIModel = AIModel.GEMINI,
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
    data class SavedModel(val model: AIModel) : SettingsEvent
    data class SavedApiKey(val apiKey: String) : SettingsEvent
}
