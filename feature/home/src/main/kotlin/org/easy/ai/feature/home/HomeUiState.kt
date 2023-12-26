package org.easy.ai.feature.home

import androidx.compose.runtime.Stable
import org.easy.ai.model.ChatMessage

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object NeedToSetup: HomeUiState

    @Stable
    data class Initialed(
        val chats: List<String>,
        val userMessage: String,
        val history: List<ChatMessage>,
        val recommended: List<String>
    ) : HomeUiState
}