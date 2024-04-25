package org.easy.ai.feature.settings.aimodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.data.repository.UserDataRepository
import org.easy.ai.model.AiModel
import javax.inject.Inject

@HiltViewModel
internal class AiModelManagerViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val supportedAiModels = AiModel.entries.toTypedArray()

    private val _uiState = MutableStateFlow(AiModelUiState())
    val uiState = _uiState.asStateFlow()

    val aiModels = userDataRepository.userData.map { userData ->
        supportedAiModels.map {
            AiModelUiModel(
                aiModel = it,
                apiKey = userData.apiKeys[it.name].orEmpty(),
                isActivated = userData.activatedModel == it
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun eventHandling(event: AiModelUiEvent) {
        when (event) {
            is AiModelUiEvent.ClickEdit -> _uiState.update {
                it.copy(inEditModel = true, selectedAi = event.aiModel, selectedKey = event.initKey)
            }

            AiModelUiEvent.EditDone -> _uiState.update {
                it.copy(inEditModel = false, selectedKey = "", selectedAi = AiModel.GEMINI)
            }

            is AiModelUiEvent.UpdateApiKey -> {
                viewModelScope.launch {
                    userDataRepository.setAiModelApiKey(event.aiModel, event.apiKey)
                }
            }
        }
    }
}