package org.easy.ai.data.repository.chat

import org.easy.ai.network.gemini.type.Content
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatGPTChatRepository @Inject constructor(): AiModelChatRepository {
    override suspend fun startChat(chatId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(apiKey: String, message: String): String {
        TODO("Not yet implemented")
    }
}