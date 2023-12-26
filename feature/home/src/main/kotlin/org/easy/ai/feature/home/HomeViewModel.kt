package org.easy.ai.feature.home

import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlobPart
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.ImagePart
import com.google.ai.client.generativeai.type.Part
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import org.easy.ai.model.UserDataValidateResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel<HomeEvent>() {
    private val _historyChats = MutableStateFlow<List<String>>(emptyList())
    private val _userMessage = MutableStateFlow("")
    private val _localHistory = MutableStateFlow<List<Content>>(emptyList())

    private lateinit var chat: Chat

    val homeUiState = combine(
        userPreferencesRepository.userData,
        _historyChats,
        _localHistory,
        _userMessage
    ) { userData, chats, history, userMessage ->
        if (userData.validate() == UserDataValidateResult.NORMAL) {
            val generativeModel = GenerativeModel(
                modelName = userData.modelName.ifBlank { "gemini-pro" },
                apiKey = userData.apiKey
            )
            chat = generativeModel.startChat(history = history)
            HomeUiState.Initialed(
                userMessage = userMessage,
                history = history.map {
                    ChatMessage(
                        text = it.parts.first().asTextOrNull() ?: "",
                        participant = if (it.role == "user") Participant.USER else Participant.MODEL,
                        isPending = false
                    )
                },
                chats = chats,
                recommended = emptyList()
            )
        } else {
            HomeUiState.NeedToSetup
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), HomeUiState.Loading)

    private val fullParts = mutableListOf<Part>()

    fun startNewChat(id: Long) {
        // get history by id
        val history: List<Content> = emptyList()
        _localHistory.update { emptyList() }
        chat.history.clear()
        chat.history.addAll(history)
    }

    fun sendMessage() {
        chat.sendMessageStream(_userMessage.value).onStart {
            _localHistory.update {
                it + content("user") { text(_userMessage.value) }
            }
            _userMessage.update { "" }
        }.onEach { response ->
            if (fullParts.isEmpty()) {
                fullParts += response.candidates.first().content.parts
                _localHistory.update { it + response.candidates.first().content }
            } else {
                fullParts += response.candidates.first().content.parts
                _localHistory.update {
                    val tempList = it.toMutableList()
                    tempList[it.lastIndex] = content("model") {
                        fullParts.forEach { part ->
                            when (part) {
                                is TextPart -> text(part.text)
                                is ImagePart -> image(part.image)
                                is BlobPart -> blob(part.mimeType, part.blob)
                            }
                        }
                    }
                    tempList
                }
            }
        }.onCompletion {
            fullParts.clear()
        }.catch {
            // add error message
            it.printStackTrace()
        }.launchIn(viewModelScope)
    }

    fun onMessageChanged(message: String) {
        _userMessage.update { message }
    }
}