package org.easy.ai.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.di.ModelPlatformQualifier
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.model.ModelRepository
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ModelPlatformQualifier(ModelPlatform.GEMINI) private val modelRepository: ModelRepository,
    @ModelPlatformQualifier(ModelPlatform.GEMINI) private val chatRepository: ChatRepository,
) : BaseViewModel<ChatEvent>() {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val _selectedChat = MutableStateFlow<AiChat?>(null)

    val chatUiState = combine(
        modelRepository.initial(),
        chatRepository.allChats(),
        _selectedChat,
        _chatHistory
    ) { isInitialed, chats, selectedChat, chatHistory ->
        println("-===== $isInitialed")
        if (isInitialed) {
            ChatUiState.Initialed(
                chats = chats,
                currentChat = selectedChat,
                chatHistory = chatHistory
            )
        } else {
            ChatUiState.Configuration
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), ChatUiState.Configuration)

    private fun sendMessage(userMessage: String) {
        _chatHistory.update {
            it + listOf(
                ChatMessage(
                    text = userMessage,
                    participant = Participant.USER,
                    isPending = false
                ),
                ChatMessage(
                    participant = Participant.MODEL,
                    isPending = true
                )
            )
        }
        viewModelScope.launch {
            val chatMessage = modelRepository.sendMessage(userMessage)
            _chatHistory.update {
                it.dropLast(1) + chatMessage
            }
        }
    }

    private fun saveChat() {
        viewModelScope.launch {
            val messages = _chatHistory.value.map { it.text }
            if (messages.isEmpty()) return@launch
            val firstMessage = messages.first()
            val name = firstMessage.take(8.coerceAtMost(firstMessage.length))
            chatRepository.saveChat(
                AiChat(
                    UUID.randomUUID().toString(),
                    name,
                    System.currentTimeMillis()
                ),
                messages = _chatHistory.value.map { it.text }
            )
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnSettingsClicked -> dispatchNavigationEvent(event)
            is ChatEvent.OnMessageSend -> sendMessage(event.userMessage)
            is ChatEvent.SaveChat -> saveChat()
        }
    }
}