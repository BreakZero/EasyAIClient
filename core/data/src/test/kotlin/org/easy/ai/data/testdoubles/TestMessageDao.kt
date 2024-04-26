package org.easy.ai.data.testdoubles

import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatHistory
import org.easy.ai.database.entities.MessageEntity

class TestMessageDao : MessageDao {
    override suspend fun getChatHistoryByChatId(chatId: String): ChatHistory {
        val chat = DataHolder.chatTable.find {
            it.chatId == chatId
        } ?: throw NoSuchElementException("not chat be found: $chatId")
        val message = DataHolder.messageTable.filter { it.chatId == chatId }
        return ChatHistory(
            chat = chat,
            messages = message
        )
    }

    override suspend fun insert(vararg data: MessageEntity) {
        DataHolder.messageTable.addAll(data)
    }
}