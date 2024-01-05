package org.easy.ai.chat

import androidx.compose.runtime.Stable
import org.easy.ai.data.model.AiChat
import org.easy.ai.model.ChatMessage

sealed interface ChatUiState {
    @Stable
    data class Initialed(
        val chats: List<AiChat> = emptyList(),
        val currentChat: AiChat? = null,
        val chatHistory: List<ChatMessage>
    ) : ChatUiState

    data object Configuration : ChatUiState
}

sealed interface ChatEvent {
    data object OnSettingsClicked : ChatEvent
    data class OnMessageSend(val userMessage: String) : ChatEvent

    data object SaveChat : ChatEvent
}