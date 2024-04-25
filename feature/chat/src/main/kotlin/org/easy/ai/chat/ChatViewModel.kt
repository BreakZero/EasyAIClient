package org.easy.ai.chat

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.repository.OfflineChatRepository
import org.easy.ai.domain.Chat
import org.easy.ai.domain.StartNewChatUseCase
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.AiModel
import org.easy.ai.model.Participant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val offlineChatRepository: OfflineChatRepository,
    startNewChatUseCase: StartNewChatUseCase,
) : BaseViewModel<ChatEvent>() {
    private val _allLocalChatStream = offlineChatRepository.getAllChats()
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
            offlineChatRepository.getMessagesByChat(it.chatId)
        } ?: emptyList()
        chat.release(newHistory.map {
            ChatMessageContent(message = it.text, participant = it.participant)
        })
        _selectedChat.update { uiChat }
        _chatHistory.update { newHistory }
    }

    private fun sendMessage(userMessage: String) {
        val uiMessageModel = ChatMessageUiModel(
            text = userMessage, participant = Participant.USER, isPending = true
        )
        _pendingMessage.update { uiMessageModel }

        chat.sendMessageStream(userMessage)
            .onEach { content ->
                _chatHistory.update {
                    it + uiMessageModel.copy(isPending = false) + ChatMessageUiModel(
                        text = content.message,
                        participant = content.participant
                    )
                }
                _selectedChat.value?.let {
                    offlineChatRepository.saveMessage(
                        chatId = it.chatId,
                        text = userMessage,
                        participant = Participant.USER
                    )
                    offlineChatRepository.saveMessage(
                        chatId = it.chatId,
                        text = content.message,
                        participant = content.participant
                    )
                }
            }.catch {
                Log.w("===", it.message.orEmpty())
            }.onCompletion { resMessage ->
                clearPendingMessage()
            }.launchIn(viewModelScope)
    }

    private fun clearPendingMessage() {
        _pendingMessage.update { null }
    }

    private suspend fun saveChat() {
        if (_selectedChat.value != null || _chatHistory.value.isEmpty()) return
        val chatId = UUID.randomUUID().toString()
        val chatName = _chatHistory.value.first().text
        offlineChatRepository.saveChat(
            chatId = chatId,
            name = chatName,
            platform = AiModel.GEMINI
        )
        _chatHistory.value.onEach {
            offlineChatRepository.saveMessage(chatId, it.text, it.participant)
        }
        _selectedChat.update {
            offlineChatRepository.getChatById(chatId)
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageSend -> sendMessage(event.userMessage)

            is ChatEvent.SelectedChat -> {
                viewModelScope.launch {
                    onChatSelected(event.chat)
                }
            }

            is ChatEvent.OnSaveChat -> {
                viewModelScope.launch {
                    saveChat()
                }
            }

            is ChatEvent.OnDeleteChat -> {
                viewModelScope.launch {
                    offlineChatRepository.deleteChatById(event.chatId)
                    onChatSelected(null)
                }
            }

            else -> dispatchNavigationEvent(event)
        }
    }
}