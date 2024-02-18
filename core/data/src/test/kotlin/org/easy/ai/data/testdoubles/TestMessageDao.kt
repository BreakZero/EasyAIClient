package org.easy.ai.data.testdoubles

import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.AiMessageEntity

class TestMessageDao: MessageDao {
    private val messages = mutableListOf<AiMessageEntity>()
    override suspend fun insert(vararg data: AiMessageEntity) {
        messages.addAll(data)
    }
}