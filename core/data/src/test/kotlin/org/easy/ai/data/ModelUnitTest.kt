package org.easy.ai.data

import org.easy.ai.data.model.AiChat
import org.easy.ai.data.model.asEntity
import org.easy.ai.database.entities.ChatEntity
import org.junit.Assert
import org.junit.Test

class ModelUnitTest {
    @Test
    fun test_ai_chat_model() {
        val aiChat = AiChat("chat_id","chat_name",8888)
        val entity = aiChat.asEntity()
        Assert.assertEquals(entity, ChatEntity("chat_id","chat_name",8888))
    }
}