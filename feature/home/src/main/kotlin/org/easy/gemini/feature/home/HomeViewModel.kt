package org.easy.gemini.feature.home

import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.easy.gemini.common.BaseViewModel
import org.easy.gemini.feature.home.model.Message
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeEvent>() {
    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val chat = generativeModel.startChat()
    private val messages = mutableListOf<Message>()

    private val responseMessageContent = StringBuilder()
    fun sendMessage() {
        chat.sendMessageStream(_message.value).onStart {
            println("===== onStart")
            messages.add(Message(false, _message.value))
            _message.update { "" }
            _homeUiState.update {
                HomeUiState.Initialed(history = messages, recommended = emptyList())
            }
        }.onEach {
            println("===== ${it.text}")
            responseMessageContent.append(it.text)
        }.onCompletion {
            println("===== onCompleted... ${it?.message}")
            _homeUiState.update {
                messages.add(Message(true, responseMessageContent.toString()))
                HomeUiState.Initialed(history = messages, recommended = emptyList())
            }
        }.catch {
            println("===== onError ${it.message}")
        }.launchIn(viewModelScope)
    }

    fun onMessageChanged(message: String) {
        _message.update { message }
    }
}