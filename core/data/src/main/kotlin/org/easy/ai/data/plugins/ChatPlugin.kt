package org.easy.ai.data.plugins

import org.easy.ai.data.model.ChatMessageContent

interface ChatPlugin {
    suspend fun sendMessage(apiKey: String, history: List<ChatMessageContent>): ChatMessageContent
}