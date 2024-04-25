package org.easy.ai.feature.settings

import androidx.compose.runtime.Stable
import org.easy.ai.model.AiModel

@Stable
data class SettingsUiState(
    val activatedAiModel: AiModel? = null
)

sealed interface SettingsEvent {
    data class AutomaticSaveChatChanged(val isChecked: Boolean): SettingsEvent
    data object ToAiModelManager: SettingsEvent
}
