package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.AiModel
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import java.util.UUID
import javax.inject.Inject

class OfflineChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) : ChatRepository {
    override fun getAllChats(): Flow<List<ChatUiModel>> {
        return chatDao.getAllChats().map { it.map(ChatEntity::asExternalModel) }
    }

    override suspend fun saveChat(chatId: String?, name: String, platform: AiModel) {
        chatDao.insert(
            ChatEntity(
                chatId = chatId ?: UUID.randomUUID().toString(),
                chatName = name,
                model = platform,
                createAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteChatById(chatId: String) {
        chatDao.deleteChatById(chatId)
    }

    override suspend fun getChatById(chatId: String): ChatUiModel {
        return chatDao.getChatById(chatId).asExternalModel()
    }

    override suspend fun saveMessage(chatId: String, text: String, participant: Participant) {
        val entity = MessageEntity(
            participant = participant,
            content = text,
            chatId = chatId,
            timestamp = System.currentTimeMillis()
        )
        messageDao.insert(entity)
    }

    override suspend fun getMessagesByChat(chatId: String): List<ChatMessage> {
        return messageDao.getChatHistoryByChatId(chatId).messages.map(MessageEntity::asExternalModel)
    }
}

