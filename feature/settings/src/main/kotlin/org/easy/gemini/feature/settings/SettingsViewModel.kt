package org.easy.gemini.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.easy.gemini.data.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    init {
        userPreferencesRepository.userData.onEach {
            println("===== $it")
        }.launchIn(viewModelScope)
    }

    fun setModelName(modelName: String) {
        userPreferencesRepository
    }
}