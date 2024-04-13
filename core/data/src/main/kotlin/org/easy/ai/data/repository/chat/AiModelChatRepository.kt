package org.easy.ai.data.repository.chat

import org.easy.ai.model.EasyPrompt

interface AiModelChatRepository {
    suspend fun startChat(chatId: String?)

    suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String
}