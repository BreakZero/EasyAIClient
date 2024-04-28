package org.easy.ai.data.aimodel

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.ChatMessage

class ChatGPTModelRepository @Inject internal constructor() : ChatPlugin {
    override suspend fun sendMessage(apiKey: String, history: List<ChatMessage>): ChatMessage {
        TODO("Not yet implemented")
    }

    override fun sendMessageStream(apiKey: String, history: List<ChatMessage>): Flow<ChatMessage> {
        TODO("Not yet implemented")
    }
}