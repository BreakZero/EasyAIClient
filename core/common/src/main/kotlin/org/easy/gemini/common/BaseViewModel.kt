package org.easy.gemini.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel<E>: ViewModel() {
    private val _navigationEvents = Channel<E>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    protected fun dispatchNavigationEvent(event: E) {
        viewModelScope.launch { _navigationEvents.send(event) }
    }
}