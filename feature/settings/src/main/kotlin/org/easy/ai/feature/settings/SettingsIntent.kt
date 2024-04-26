package org.easy.ai.feature.settings

import androidx.compose.runtime.Stable
import org.easy.ai.model.AiModel

@Stable
data class SettingsUiState(
    val defaultChatAi: AiModel? = null,
    val showAiSelector: Boolean = false
)

sealed interface SettingsEvent {
    data class OnChatAiChanged(val aiModel: AiModel): SettingsEvent

    data object OpenSelector: SettingsEvent
    data object CloseSelector: SettingsEvent
    data object NavigateToAbout: SettingsEvent
    data object NavigateToAiModelManager: SettingsEvent
}
