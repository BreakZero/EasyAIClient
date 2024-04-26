package org.easy.ai.chat

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
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.repository.OfflineChatRepository
import org.easy.ai.domain.Chat
import org.easy.ai.domain.StartNewChatUseCase
import org.easy.ai.model.AiModel
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.MessageType
import org.easy.ai.model.Participant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val offlineChatRepository: OfflineChatRepository,
    startNewChatUseCase: StartNewChatUseCase,
) : BaseViewModel<ChatEvent>() {
    private val _allLocalChatStream = offlineChatRepository.getAllChats()
    private val _pendingMessage = MutableStateFlow<ChatMessage?>(null)
    private val _chatHistory = MutableStateFlow(emptyList<ChatMessage>())
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
        chat.release(newHistory)
        _selectedChat.update { uiChat }
        _chatHistory.update { newHistory }
    }

    private fun sendMessage(userMessage: String) {
        val sendingMessage = ChatMessage(
            content = userMessage, participant = Participant.USER, type = MessageType.PENDING
        )
        _pendingMessage.update { sendingMessage }

        chat.sendMessageStream(userMessage)
            .onEach { message ->
                if (message.type == MessageType.ERROR) {
                    // handle error response, ignore save into local
                    _chatHistory.update {
                        it + sendingMessage.copy(type = MessageType.ERROR) + message
                    }
                } else {
                    // handle success response, save into local
                    _selectedChat.value?.run {
                        offlineChatRepository.saveMessage(
                            chatId = chatId,
                            text = userMessage,
                            participant = Participant.USER
                        )
                        offlineChatRepository.saveMessage(
                            chatId = chatId,
                            text = message.content,
                            participant = message.participant
                        )
                    }
                    _chatHistory.update {
                        it + sendingMessage.copy(type = MessageType.SUCCESS) + message
                    }
                }
            }.onCompletion { _ ->
                clearPendingMessage()
            }.launchIn(viewModelScope)
    }

    private fun clearPendingMessage() {
        _pendingMessage.update { null }
    }

    private suspend fun saveChat() {
        if (_selectedChat.value != null || _chatHistory.value.isEmpty()) return
        val chatId = UUID.randomUUID().toString()
        val chatName = _chatHistory.value.first().content
        offlineChatRepository.saveChat(
            chatId = chatId,
            name = chatName,
            platform = AiModel.GEMINI
        )
        _chatHistory.value.onEach {
            offlineChatRepository.saveMessage(chatId, it.content, it.participant)
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
                    if (_selectedChat.value?.chatId == event.chatId){
                        onChatSelected(null)
                    }
                }
            }

            else -> dispatchNavigationEvent(event)
        }
    }
}