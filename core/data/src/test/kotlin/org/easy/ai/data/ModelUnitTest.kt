package org.easy.ai.data

import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.data.model.asEntity
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.AiModel
import org.junit.Assert
import org.junit.Test

class ModelUnitTest {
    @Test
    fun test_ai_chat_model() {
        val chatUiModel = ChatUiModel("chat_id", "chat_name", 888)
        val entity = chatUiModel.asEntity()
        Assert.assertEquals(entity, ChatEntity("chat_id", "chat_name", AiModel.GEMINI, 888))
    }
}