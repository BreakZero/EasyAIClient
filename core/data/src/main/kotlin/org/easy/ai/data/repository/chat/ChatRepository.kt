package org.easy.ai.data.repository.chat

import org.easy.ai.model.EasyPrompt

interface ChatRepository {
    fun startChat()

    suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String

    suspend fun addChat()

    suspend fun deletedById(chatId: String)

}