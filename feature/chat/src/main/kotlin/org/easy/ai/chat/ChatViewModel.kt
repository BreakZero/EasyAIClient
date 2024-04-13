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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val localChatRepository: LocalChatRepository,
    private val startChatUseCase: StartChatUseCase,
    private val messageSendingUseCase: MessageSendingUseCase
) : BaseViewModel<ChatEvent>() {
    private val _chatHistory = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    private val _selectedChat = MutableStateFlow<ChatUiModel?>(null)

    val chatUiState = combine(
        userPreferencesRepository.hasApiKey(),
        localChatRepository.getAllChats(),
        _selectedChat,
    ) { hasApiKey, chats, selectedChat ->
        if (hasApiKey) {
            val chatHistory = selectedChat?.let {
                localChatRepository.getMessagesByChat(it.chatId)
            } ?: emptyList()
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
        startChatUseCase(chat?.chatId).onCompletion {
            _selectedChat.update { chat }

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