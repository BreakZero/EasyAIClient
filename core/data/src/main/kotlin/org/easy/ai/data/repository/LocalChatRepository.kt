package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import javax.inject.Inject

class LocalChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
): ChatRepository {
    override fun getAllChats(): Flow<List<ChatUiModel>> {
        return chatDao.getAllChats().map { it.map(ChatEntity::asExternalModel) }
    }

    override suspend fun saveChat(name: String, platform: ModelPlatform) {
//        chatDao.insert()
    }

    override suspend fun saveMessage(chatId: String, text: String, participant: Participant) {
//        messageDao.insert(message.asEntity(chatId))
    }

    override suspend fun getMessagesByChat(chatId: String): List<ChatMessageUiModel> {
        return chatDao.getChatWithMessages(chatId).messages.map(AiMessageEntity::asExternalModel)
    }
}

