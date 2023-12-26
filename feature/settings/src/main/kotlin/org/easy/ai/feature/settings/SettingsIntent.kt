package org.easy.ai.feature.settings

import androidx.compose.runtime.Stable
import org.easy.ai.model.AIModel
import org.easy.ai.model.ModelPlatform

@Stable
data class SettingsUiState(
    val modelPlatform: ModelPlatform = ModelPlatform.GEMINI,
    val apiKey: String = "",
    val model: AIModel = AIModel.GeminiPro
)

sealed interface SettingsEvent {
    data class SavedModel(val model: AIModel): SettingsEvent
    data class SavedApiKey(val apiKey: String): SettingsEvent
}


