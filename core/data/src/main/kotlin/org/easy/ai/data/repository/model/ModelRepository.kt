package org.easy.ai.data.repository.model

import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.ChatMessage

interface ModelRepository {
    fun initial(): Flow<Boolean>

    fun switchChat(history: List<ChatMessage>)
    suspend fun sendMessage(userMessage: String): ChatMessage

    suspend fun generateTextFromMultiModal(prompt: Content): String
}