package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.model.AiMessage

interface ChatRepository {
    fun allChats(): Flow<List<AiChat>>
    suspend fun saveChat(aiChat: AiChat, messages: List<String>)
    fun getMessagesByChat(chatId: String):  Flow<List<AiMessage>>
}