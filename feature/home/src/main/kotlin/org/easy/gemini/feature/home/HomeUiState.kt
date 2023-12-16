package org.easy.gemini.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Initialed(
        val history: List<String>,
        val recommended: List<String>
    ) : HomeUiState
}