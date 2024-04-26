package org.easy.ai.data.plugins

import org.easy.ai.model.ChatMessage

interface ChatPlugin {
    suspend fun sendMessage(apiKey: String, history: List<ChatMessage>): ChatMessage
}