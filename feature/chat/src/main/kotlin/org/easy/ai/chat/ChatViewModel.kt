package org.easy.ai.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.domain.MessageSendingUseCase
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.EasyPrompt
import org.easy.ai.model.Participant
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val localChatRepository: LocalChatRepository,
    private val messageSendingUseCase: MessageSendingUseCase
) : BaseViewModel<ChatEvent>() {
    private val _chatHistory = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    private val _selectedChat = MutableStateFlow<ChatUiModel?>(null)

    val chatUiState = combine(
        localChatRepository.getAllChats(),
        _selectedChat,
        _chatHistory,
    ) { chats, selectedChat, chathistory ->
        val chatHistory = selectedChat?.let {
            localChatRepository.getMessagesByChat(it.chatId)
        } ?: emptyList()
        ChattingUiState(
            chats = chats,
            selectedChat = selectedChat,
            chatHistory = chathistory
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), ChattingUiState())

    private fun sendMessage(userMessage: String) {
        val userPrompt = EasyPrompt.TextPrompt(role = "user", userMessage)
        val sendingMessage = ChatMessageUiModel(text = userMessage, isPending = true)
        _chatHistory.update {
            it + sendingMessage
        }
        messageSendingUseCase(userPrompt).onEach { response ->
            _chatHistory.update {
                it + ChatMessageUiModel(text = response, participant = Participant.MODEL)
            }
        }.catch { error ->
            _chatHistory.update {
                it + ChatMessageUiModel(text = "response error", participant = Participant.ERROR)
            }
            error.printStackTrace()
        }.launchIn(viewModelScope)
    }

    private fun onSelectedChat(chat: ChatUiModel?) {
        _selectedChat.update { chat }
        chat?.let {
            viewModelScope.launch {
                val messages = localChatRepository.getMessagesByChat(it.chatId)
                // start new chat
            }
        } ?: run {
            _chatHistory.update { emptyList() }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageSend -> sendMessage(event.userMessage)
            is ChatEvent.SelectedChat -> onSelectedChat(event.chat)
            else -> dispatchNavigationEvent(event)
        }
    }
}