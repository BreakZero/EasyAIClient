package org.easy.ai.feature.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.repository.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val offlineUserDataRepository: UserDataRepository
) : BaseViewModel<SettingsEvent>() {

    val settingsUiState = offlineUserDataRepository.userData.map {
        println("===== $it")
        SettingsUiState(activatedAiModel = it.activatedModel)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), SettingsUiState())

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.AutomaticSaveChatChanged -> Unit
            is SettingsEvent.ToAiModelManager -> dispatchNavigationEvent(event)
        }
    }
}