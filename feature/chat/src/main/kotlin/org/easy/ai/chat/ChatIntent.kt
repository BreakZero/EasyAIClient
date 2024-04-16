package org.easy.ai.chat

import androidx.compose.runtime.Stable
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.model.ChatMessageUiModel

@Stable
data class ChattingUiState(
    val chats: List<ChatUiModel> = emptyList(),
    val selectedChat: ChatUiModel? = null,
    val pendingMessage: ChatMessageUiModel? = null,
    val chatHistory: List<ChatMessageUiModel> = emptyList()
)

sealed interface ChatUiState {
    data object Initialed: ChatUiState

    data object NoApiSetup: ChatUiState
}

sealed interface ChatEvent {
    data object OnSettingsClicked : ChatEvent
    data class OnMessageSend(val userMessage: String) : ChatEvent
    data class SelectedChat(val chat: ChatUiModel?) : ChatEvent
    data object OnPluginsClicked : ChatEvent

    data object OnSaveChat: ChatEvent
    data class OnDeleteChat(val chatId: String): ChatEvent
}