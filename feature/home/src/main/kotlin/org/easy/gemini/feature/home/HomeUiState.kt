package org.easy.gemini.feature.home

import org.easy.gemini.feature.home.model.Message

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Initialed(
        val history: List<Message>,
        val recommended: List<String>
    ) : HomeUiState
}