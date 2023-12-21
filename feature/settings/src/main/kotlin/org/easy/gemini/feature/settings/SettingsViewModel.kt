package org.easy.gemini.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.easy.gemini.data.repository.UserPreferencesRepository
import org.easy.gemini.model.UserData
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val settingsUiState = userPreferencesRepository.userData
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(3_000), UserData("", ""))

    fun onModelNameChanged(modelName: String) {
        viewModelScope.launch {
            userPreferencesRepository.setModelName(modelName)
        }
    }

    fun setApiKey(apiKey: String) {
        viewModelScope.launch {
            userPreferencesRepository.setApiKey(apiKey)
        }
    }

}