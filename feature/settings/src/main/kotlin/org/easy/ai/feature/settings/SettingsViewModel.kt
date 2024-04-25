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
import org.easy.ai.data.repository.UserDataRepository
import org.easy.ai.model.AiModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val offlineUserDataRepository: UserDataRepository
) : BaseViewModel<SettingsEvent>() {
    private val _defaultUiState = MutableStateFlow(SettingsUiState())

    val settingsUiState =
        combine(_defaultUiState, offlineUserDataRepository.userData) { defaultState, userData ->
            defaultState.copy(
                apiKey = userData.apiKeys[AiModel.GEMINI.name].orEmpty(),
                model = userData.activatedModel,
                isAutomaticSaveChat = userData.isAutomaticSaveChat
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), SettingsUiState())

    private fun updateAiModel(model: AiModel) {
        viewModelScope.launch {
            offlineUserDataRepository.selectedAiModel(model)
        }
    }

    private fun updateApiKey(apiKey: String) {
        viewModelScope.launch {
            offlineUserDataRepository.setAiModelApiKey(AiModel.GEMINI, apiKey)
        }
    }

    private fun updateAutomaticSave(isAutomatic: Boolean) {

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

            is SettingsEvent.SavedModel -> updateAiModel(event.model)
            is SettingsEvent.SavedApiKey -> updateApiKey(event.apiKey)
            is SettingsEvent.AutomaticSaveChatChanged -> updateAutomaticSave(event.isChecked)
            is SettingsEvent.ToAiModelManager -> dispatchNavigationEvent(event)
        }
    }
}