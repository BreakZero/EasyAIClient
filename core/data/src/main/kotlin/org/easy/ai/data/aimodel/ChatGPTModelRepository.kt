package org.easy.ai.data.aimodel

import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.plugins.ChatPlugin
import javax.inject.Inject

class ChatGPTModelRepository @Inject internal constructor(): ChatPlugin {
    override suspend fun sendMessage(
        apiKey: String,
        history: List<ChatMessageContent>
    ): ChatMessageContent {
        TODO("Not yet implemented")
    }
}