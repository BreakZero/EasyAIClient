package org.easy.ai.data.aimodel

import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.ChatMessage
import javax.inject.Inject

class ChatGPTModelRepository @Inject internal constructor(): ChatPlugin {
    override suspend fun sendMessage(
        apiKey: String,
        history: List<ChatMessage>
    ): ChatMessage {
        TODO("Not yet implemented")
    }
}