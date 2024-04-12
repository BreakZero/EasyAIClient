package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.data.model.AiChat
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant

interface ChatRepository {
    fun getAllChats(): Flow<List<AiChat>>
    suspend fun saveChat(name: String, platform: ModelPlatform)

    suspend fun saveMessage(chatId: String, text: String, participant: Participant)

    suspend fun getMessagesByChat(chatId: String): List<ChatMessage>
}