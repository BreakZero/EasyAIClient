package org.easy.ai.data.repository.model

import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.ChatMessage

interface ModelRepository {
    fun initial(): Flow<Boolean>
    suspend fun sendMessage(userMessage: String): ChatMessage
}