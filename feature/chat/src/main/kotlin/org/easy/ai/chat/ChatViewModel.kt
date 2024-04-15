package org.easy.ai.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.domain.Chat
import org.easy.ai.domain.StartNewChatUseCase
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.Participant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val localChatRepository: LocalChatRepository,
    private val startNewChatUseCase: StartNewChatUseCase,
) : BaseViewModel<ChatEvent>() {
    private val _allLocalChatStream = localChatRepository.getAllChats()
    private val _pendingMessage = MutableStateFlow<ChatMessageUiModel?>(null)
    private val _chatHistory = MutableStateFlow(emptyList<ChatMessageUiModel>())
    private val _selectedChat = MutableStateFlow<ChatUiModel?>(null)

    private lateinit var chat: Chat

    val chatUiState = startNewChatUseCase().map {
        this.chat = it
        ChatUiState.Initialed as ChatUiState
    }.catch {
        emit(ChatUiState.NoApiSetup)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), ChatUiState.NoApiSetup)

    val chatContentUiState = combine(
        _allLocalChatStream,
        _pendingMessage,
        _chatHistory,
        _selectedChat
    ) { chats, pendingMessage, history, selectedChat ->
        ChattingUiState(
            chats = chats,
            pendingMessage = pendingMessage,
            chatHistory = history,
            selectedChat = selectedChat
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), ChattingUiState())

    private suspend fun onChatSelected(uiChat: ChatUiModel?) {
        val newHistory = uiChat?.let {
            localChatRepository.getMessagesByChat(it.chatId)
        } ?: emptyList()
        chat.release(newHistory.map {
            ChatMessageContent(message = it.text, participant = it.participant)
        })
        _selectedChat.update { uiChat }
        _chatHistory.update { newHistory }
    }

    private suspend fun sendMessage(userMessage: String) {
        val uiMessageModel = ChatMessageUiModel(
            text = userMessage, participant = Participant.USER, isPending = true
        )
        _pendingMessage.update { uiMessageModel }
        if (_selectedChat.value != null) {
            // save message
        }
        val response = chat.sendMessage(userMessage)
        clearPendingMessage()
        if (_selectedChat.value != null) {
            // save message
        }
        _chatHistory.update {
            it + uiMessageModel + ChatMessageUiModel(
                text = response.message,
                participant = response.participant
            )
        }
    }

    private fun clearPendingMessage() {
        _pendingMessage.update { null }
    }


    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageSend -> viewModelScope.launch {
                sendMessage(event.userMessage)
            }

            is ChatEvent.SelectedChat -> {
                viewModelScope.launch {
                    onChatSelected(event.chat)
                }
            }

            is ChatEvent.OnSaveChat -> {}
            else -> dispatchNavigationEvent(event)
        }
    }
}