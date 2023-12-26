package org.easy.ai.feature.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel<SettingsEvent>() {
    val settingsUiState = userPreferencesRepository.userData.map {
        SettingsUiState(apiKey = it.apiKey)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), SettingsUiState())

    private fun savedModel(modelName: String) {
        viewModelScope.launch {
            userPreferencesRepository.setModelName(modelName)
        }
    }

    private fun savedApiKey(apiKey: String) {
        viewModelScope.launch {
            userPreferencesRepository.setApiKey(apiKey)
        }
    }


    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SavedModel -> savedModel(event.model.model)
            is SettingsEvent.SavedApiKey -> savedApiKey(event.apiKey)
        }
    }
}