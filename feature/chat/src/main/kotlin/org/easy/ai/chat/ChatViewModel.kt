package org.easy.ai.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.domain.MessageSendingUseCase
import org.easy.ai.domain.StartChatUseCase
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.EasyPrompt
import org.easy.ai.model.Participant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    private val localChatRepository: LocalChatRepository,
    private val startChatUseCase: StartChatUseCase,
    private val messageSendingUseCase: MessageSendingUseCase
) : BaseViewModel<ChatEvent>() {
    private val _selectedChat = MutableStateFlow<ChatUiModel?>(null)
    private val _messageSize = MutableStateFlow(0)

    private val _chatHistoryFlow = combine(_messageSize, _selectedChat) { _, chat ->
        chat?.chatId?.let { localChatRepository.getMessagesByChat(it) } ?: emptyList()
    }

    private val _pendingMessage = MutableStateFlow<ChatMessageUiModel?>(null)
    val pendingMessage = _pendingMessage.asStateFlow()

    val chatUiState = combine(
        userPreferencesRepository.hasApiKey(),
        localChatRepository.getAllChats(),
        _selectedChat,
        _chatHistoryFlow
    ) { isHasKeys, chats, selectedChat, chatHistory ->
        if (isHasKeys) {
            ChatUiState.Chatting(
                chats = chats,
                selectedChat = selectedChat,
                chatHistory = chatHistory
            )
        } else {
            ChatUiState.NoApiSetup
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), ChatUiState.NoApiSetup)

    private fun sendMessage(userMessage: String) {
        val userPrompt = EasyPrompt.TextPrompt(role = "user", userMessage)
        val uiMessageModel = ChatMessageUiModel(text = userMessage, participant = Participant.USER, isPending = true)
        _pendingMessage.update { uiMessageModel }

        messageSendingUseCase(userPrompt).onEach { _ ->
            clearPendingMessage()
            _messageSize.update { it + 1 }
        }.catch { error ->
            clearPendingMessage()
            _messageSize.update { it + 1 }
            error.printStackTrace()
        }.launchIn(viewModelScope)
    }

    private fun clearPendingMessage() {
        _pendingMessage.update { null }
    }

    private fun onSelectedChat(chat: ChatUiModel?) {
        startChatUseCase(chat?.chatId).onEach {
            clearPendingMessage()
            _selectedChat.update { chat }
            val historySize = chat?.chatId?.let {
                localChatRepository.getMessagesByChat(it).size
            } ?: 0
            _messageSize.update { historySize }
        }.catch {
            it.printStackTrace()
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageSend -> sendMessage(event.userMessage)
            is ChatEvent.SelectedChat -> onSelectedChat(event.chat)
            else -> dispatchNavigationEvent(event)
        }
    }
}