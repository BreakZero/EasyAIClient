package org.easy.ai.data.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.ChatWithMessage

class TestChatDao: ChatDao {
    private val mockChats = mutableListOf(ChatEntity("chat_id","chat_name",888))
    override fun getAllChats(): Flow<List<ChatEntity>> {
        return flow { emit(mockChats) }
    }

    override suspend fun getChatWithMessages(chatId: String): ChatWithMessage {
        return ChatWithMessage(
            chat = ChatEntity("chat_id","chat_name",888)
        )
    }

    override suspend fun insert(vararg data: ChatEntity) {
        mockChats.addAll(data)
    }
}