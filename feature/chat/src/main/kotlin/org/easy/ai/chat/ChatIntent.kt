package org.easy.ai.chat

import androidx.compose.runtime.Stable
import org.easy.ai.model.ChatMessage

sealed interface ChatUiState {
    @Stable
    data class Initialed(
        val userMessage: String,
        val chatHistory: List<ChatMessage>
    ) : ChatUiState

    data object Configuration : ChatUiState
}

sealed interface ChatEvent {
    data object OnSettingsClicked: ChatEvent
    data class OnMessageSend(val userMessage: String): ChatEvent
    data class OnUserMessageChanged(val userMessage: String): ChatEvent
}