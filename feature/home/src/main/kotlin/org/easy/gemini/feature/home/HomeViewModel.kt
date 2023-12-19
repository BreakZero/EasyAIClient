package org.easy.gemini.feature.home

import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlobPart
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.ImagePart
import com.google.ai.client.generativeai.type.Part
import com.google.ai.client.generativeai.type.TextPart
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
import org.easy.gemini.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeEvent>() {
    private val _historyChats = MutableStateFlow<List<String>>(emptyList())
    private val _message = MutableStateFlow("")
    private val _localHistory = MutableStateFlow<List<Content>>(emptyList())

    val homeUiState = combine(_historyChats, _localHistory, _message) { chats, history, message ->
        HomeUiState.Initialed(
            message = message,
            history = history,
            chats = chats,
            recommended = emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), HomeUiState.Loading)

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private var chat: Chat? = null
    private val fullParts = mutableListOf<Part>()

    fun startNewChat(history: List<Content>) {
        chat = generativeModel.startChat(
            history = history.also { initHistory ->
                _localHistory.update { initHistory }
            }
        )
    }

    fun sendMessage() {
        if (chat == null) {
            startNewChat(emptyList())
        }
        chat!!.sendMessageStream(_message.value).onStart {
            _localHistory.update {
                it + content("user") { text(_message.value) }
            }
            _message.update { "" }
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
        _message.update { message }
    }
}