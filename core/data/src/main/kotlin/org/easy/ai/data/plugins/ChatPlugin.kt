package org.easy.ai.data.plugins

import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.ChatMessage

interface ChatPlugin {
    suspend fun sendMessage(apiKey: String, history: List<ChatMessage>): ChatMessage

    fun sendMessageStream(apiKey: String, history: List<ChatMessage>): Flow<ChatMessage>
}