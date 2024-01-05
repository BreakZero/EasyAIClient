package org.easy.ai.feature.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.model.AIModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel<SettingsEvent>() {
    private val _defaultUiState = MutableStateFlow(SettingsUiState())

    val settingsUiState =
        combine(_defaultUiState, userPreferencesRepository.userData) { defaultState, userData ->
            defaultState.copy(
                apiKey = userData.apiKey,
                model = AIModel.fromModelName(userData.modelName)
            )
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
            is SettingsEvent.ShowApiKeyEditor -> {
                _defaultUiState.update { it.copy(isApiKeyEditorShowed = true) }
            }

            is SettingsEvent.HideApiKeyEditor -> {
                _defaultUiState.update { it.copy(isApiKeyEditorShowed = false) }
            }

            is SettingsEvent.ShowModelList -> {
                _defaultUiState.update { it.copy(isModelListShowed = true) }
            }

            is SettingsEvent.HideModelList -> {
                _defaultUiState.update { it.copy(isModelListShowed = false) }
            }

            is SettingsEvent.SavedModel -> savedModel(event.model.model)
            is SettingsEvent.SavedApiKey -> savedApiKey(event.apiKey)
        }
    }
}