package org.easy.ai.data.repository.model

import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.ChatMessage

interface ModelChatRepository {
    fun initial(): Flow<Boolean>

    fun switchChat(history: List<ChatMessage>)
    suspend fun sendMessage(userMessage: String): ChatMessage
}