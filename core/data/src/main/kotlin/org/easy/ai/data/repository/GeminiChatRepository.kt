package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.model.asEntity
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.ChatMessage
import javax.inject.Inject

class GeminiChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatRepository {

    override fun allChats(): Flow<List<AiChat>> {
        return chatDao.getAllChats().map { it.map(ChatEntity::asExternalModel) }
    }

    override suspend fun saveChat(aiChat: AiChat) {
        chatDao.insert(aiChat.asEntity())
    }

    override suspend fun saveMessage(chatId: String, message: ChatMessage) {
        messageDao.insert(message.asEntity(chatId))
    }

    override suspend fun getMessagesByChat(chatId: String): List<ChatMessage> {
        return chatDao.getChatWithMessages(chatId).messages.map(AiMessageEntity::asExternalModel)
    }
}