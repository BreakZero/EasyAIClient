package org.easy.gemini.feature.home

import androidx.compose.runtime.Stable
import com.google.ai.client.generativeai.type.Content

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object NeedToSetup: HomeUiState

    @Stable
    data class Initialed(
        val chats: List<String>,
        val message: String,
        val history: List<Content>,
        val recommended: List<String>
    ) : HomeUiState
}