package org.easy.gemini.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.easy.gemini.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel<HomeEvent>() {
    private val _homeUiState = MutableStateFlow(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun onMessageChanged(message: String) {
        _message.update { message }
    }
}