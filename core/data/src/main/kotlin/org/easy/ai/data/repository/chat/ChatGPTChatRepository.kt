package org.easy.ai.data.repository.chat

import org.easy.ai.model.EasyPrompt
import javax.inject.Inject

class ChatGPTChatRepository @Inject constructor(): AiModelChatRepository {
    override suspend fun startChat(chatId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String {
        TODO("Not yet implemented")
    }
}