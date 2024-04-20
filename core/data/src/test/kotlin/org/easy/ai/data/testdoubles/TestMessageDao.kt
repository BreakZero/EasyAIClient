package org.easy.ai.data.testdoubles

import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.ChatHistory
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant

class TestMessageDao : MessageDao {
    private val chatEntity = ChatEntity("chat_id", "chat_name", ModelPlatform.GEMINI, 888)
    private val mockMessages = mutableListOf(
        MessageEntity(
            messageId = 0L,
            participant = Participant.USER,
            content = "mock message content",
            chatId = "chat_id",
            timestamp = 999
        )
    )

    override suspend fun getChatHistoryByChatId(chatId: String): ChatHistory {
        println("==== $chatId")
        return ChatHistory(
            chat = chatEntity,
            messages = mockMessages
        )
    }

    override suspend fun insert(vararg data: MessageEntity) {
        mockMessages.addAll(data)
    }
}