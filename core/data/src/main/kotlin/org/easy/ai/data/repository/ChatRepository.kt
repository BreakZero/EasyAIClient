package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.data.model.AiChat
import org.easy.ai.model.ChatMessage

interface ChatRepository {
    fun allChats(): Flow<List<AiChat>>
    suspend fun saveChat(aiChat: AiChat)

    suspend fun saveMessage(chatId: String, message: ChatMessage)

    suspend fun getMessagesByChat(chatId: String):  List<ChatMessage>
}