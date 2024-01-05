package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.model.AiMessage
import org.easy.ai.data.model.asEntity
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.database.entities.ChatEntity
import java.util.UUID
import javax.inject.Inject

class GeminiChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatRepository {

    override fun allChats(): Flow<List<AiChat>> {
        return chatDao.getAllChats().map { it.map(ChatEntity::asExternalModel) }
    }

    override suspend fun saveChat(aiChat: AiChat, messages: List<String>) {
        chatDao.insert(aiChat.asEntity())
        messages.map {
            AiMessageEntity(
                id = UUID.randomUUID().toString(),
                text = it,
                belong = aiChat.chatId,
                timestamp = System.currentTimeMillis()
            )
        }.forEach {
            messageDao.insert(it)
        }
    }

    override fun getMessagesByChat(chatId: String): Flow<List<AiMessage>> {
        return chatDao.getChatWithMessages(chatId).map { it.messages }.map { it.map(AiMessageEntity::asExternalModel) }
    }
}