package org.easy.ai.chat

import androidx.compose.runtime.Stable
import org.easy.ai.data.model.AiChat
import org.easy.ai.model.ChatMessage

@Stable
data class ChattingUiState(
    val chats: List<AiChat> = emptyList(),
    val selectedChat: AiChat? = null,
    val chatHistory: List<ChatMessage> = emptyList()
)

sealed interface ChatUiState {
    @Stable
    data class Chatting(
        val chats: List<AiChat> = emptyList(),
        val selectedChat: AiChat? = null,
        val chatHistory: List<ChatMessage> = emptyList()
    )

    data object NoApiSetup: ChatUiState
}

sealed interface ChatEvent {
    data object OnSettingsClicked : ChatEvent
    data class OnMessageSend(val userMessage: String) : ChatEvent
    data class SelectedChat(val chat: AiChat?) : ChatEvent
    data object OnMultiModalClicked : ChatEvent
}