package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.model.AiModel
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant

interface ChatRepository {
    fun getAllChats(): Flow<List<ChatUiModel>>
    suspend fun saveChat(chatId: String?, name: String, platform: AiModel)

    suspend fun deleteChatById(chatId: String)

    suspend fun getChatById(chatId: String): ChatUiModel

    suspend fun saveMessage(chatId: String, text: String, participant: Participant)

    suspend fun getMessagesByChat(chatId: String): List<ChatMessage>
}