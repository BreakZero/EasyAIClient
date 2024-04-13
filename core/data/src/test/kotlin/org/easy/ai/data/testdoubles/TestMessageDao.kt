package org.easy.ai.data.testdoubles

import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.MessageEntity

class TestMessageDao: MessageDao {
    override suspend fun insert(vararg data: MessageEntity) {
        TODO("Not yet implemented")
    }

}