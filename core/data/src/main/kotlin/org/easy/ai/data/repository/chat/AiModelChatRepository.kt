package org.easy.ai.data.repository.chat


interface AiModelChatRepository {
    suspend fun startChat(chatId: String?)

    suspend fun sendMessage(apiKey: String, message: String): String
}