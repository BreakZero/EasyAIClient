package org.easy.ai.feature.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.repository.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val offlineUserDataRepository: UserDataRepository
) : BaseViewModel<SettingsEvent>() {

    private val selectorSwitcher = MutableStateFlow(false)

    val settingsUiState =
        combine(selectorSwitcher, offlineUserDataRepository.userData) { switcher, userData ->
            SettingsUiState(defaultChatAi = userData.defaultChatModel, showAiSelector = switcher)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), SettingsUiState())

    fun onEvent(event: SettingsEvent) {
        val handler = CoroutineExceptionHandler { _, exception ->

        }
        viewModelScope.launch(handler) {

        }
        when (event) {
            is SettingsEvent.OnChatAiChanged -> {
                viewModelScope.launch {
                    offlineUserDataRepository.setDefaultChatAiModel(event.aiModel)
                }
            }

            SettingsEvent.OpenSelector -> selectorSwitcher.update { true }
            SettingsEvent.CloseSelector -> selectorSwitcher.update { false }
            SettingsEvent.NavigateToAiModelManager, SettingsEvent.NavigateToAbout -> dispatchNavigationEvent(
                event
            )
        }
    }
}