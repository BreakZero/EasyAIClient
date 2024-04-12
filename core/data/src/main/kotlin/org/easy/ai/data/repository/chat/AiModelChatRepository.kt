package org.easy.ai.data.repository.chat

import org.easy.ai.model.EasyPrompt

interface AiModelChatRepository {
    fun startChat(history: List<EasyPrompt>)

    suspend fun sendMessage(apiKey: String, prompt: EasyPrompt): String
}