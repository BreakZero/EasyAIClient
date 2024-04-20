package org.easy.ai.data.testdoubles

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.ChatHistory
import org.easy.ai.model.ModelPlatform

class TestChatDao : ChatDao {
    private val mockChats =
        mutableListOf(ChatEntity("chat_id", "chat_name", ModelPlatform.GEMINI, 888))

    override fun getAllChats(): Flow<List<ChatEntity>> {
        return flow { emit(mockChats) }
    }

    override suspend fun getChatById(chatId: String): ChatEntity {
        return mockChats.find { it.chatId == chatId } ?: throw NoSuchElementException("not chat found: $chatId")
    }

    override suspend fun deleteChatById(chatId: String) {
        mockChats.removeIf {
            it.chatId == chatId
        }
    }

    override suspend fun insert(vararg data: ChatEntity) {
        mockChats.addAll(data)
    }
}