package org.easy.ai.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.di.ModelPlatformQualifier
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.UserPreferencesRepository
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
    userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel<ChatEvent>() {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val _selectedChat = MutableStateFlow<AiChat?>(null)
    private var _isAutomaticSaveChatOn = false

    init {
        viewModelScope.launch {
            userPreferencesRepository.userData
                .onEach { _isAutomaticSaveChatOn = it.isAutomaticSaveChat }
                .collect()
        }
    }

    val chatUiState = combine(
        modelRepository.initial(),
        chatRepository.allChats(),
        _selectedChat,
        _chatHistory
    ) { isInitialed, chats, selectedChat, chatHistory ->
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
        saveChat(userMessage, _chatHistory.value)
        _chatHistory.update { it ->
            it + listOf(
                ChatMessage(
                    text = userMessage,
                    participant = Participant.USER,
                    isPending = false
                ).also { saveMessage(it) },
                ChatMessage(
                    participant = Participant.MODEL,
                    isPending = true
                )
            )
        }
        viewModelScope.launch {
            val chatMessage = modelRepository.sendMessage(userMessage)
            saveMessage(chatMessage)
            _chatHistory.update {
                it.dropLast(1) + chatMessage
            }
        }
    }

    private fun saveChat(firstText: String, initMessages: List<ChatMessage>) {
        if (_selectedChat.value != null || !_isAutomaticSaveChatOn) return
        val name = firstText.take(8.coerceAtMost(firstText.length))
        val aiChat = AiChat(
            UUID.randomUUID().toString(),
            name,
            System.currentTimeMillis()
        )
        _selectedChat.update { aiChat }
        viewModelScope.launch {
            chatRepository.saveChat(aiChat)
            initMessages.forEach {
                chatRepository.saveMessage(aiChat.chatId, it)
            }
        }
    }

    private fun saveMessage(message: ChatMessage) {
        if (_selectedChat.value == null || !_isAutomaticSaveChatOn) return
        println("===== trigger save message: ${message.text}")
        viewModelScope.launch {
            val chatId = _selectedChat.value!!.chatId
            chatRepository.saveMessage(chatId, message)
        }
    }

    private fun onSelectedChat(chat: AiChat?) {
        _selectedChat.update { chat }
        chat?.let {
            viewModelScope.launch {
                val messages = chatRepository.getMessagesByChat(it.chatId)
                _chatHistory.update {
                    messages
                }
            }
        } ?: run {
            _chatHistory.update { emptyList() }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnSettingsClicked -> dispatchNavigationEvent(event)
            is ChatEvent.OnMessageSend -> sendMessage(event.userMessage)
            is ChatEvent.SelectedChat -> onSelectedChat(event.chat)
        }
    }
}