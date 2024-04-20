package org.easy.ai.data.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.entities.ChatEntity

class TestChatDao : ChatDao {
    override fun getAllChats(): Flow<List<ChatEntity>> {
        return flow { emit(DataHolder.chatTable.toList()) }
    }

    override suspend fun getChatById(chatId: String): ChatEntity {
        return DataHolder.chatTable.find { it.chatId == chatId }
            ?: throw NoSuchElementException("not chat found: $chatId")
    }

    override suspend fun deleteChatById(chatId: String) {
        DataHolder.chatTable.removeIf { it.chatId == chatId }
    }

    override suspend fun insert(vararg data: ChatEntity) {
        DataHolder.chatTable.addAll(data)
    }
}