package org.easy.ai.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.easy.ai.data.model.AiChat
import org.easy.ai.data.model.asEntity
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.data.testdoubles.TestChatDao
import org.easy.ai.data.testdoubles.TestMessageDao
import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.ChatMessage
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


class GeminiLocalChatRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var messageDao: TestMessageDao
    private lateinit var chatDao: TestChatDao
    private lateinit var geminiLocalChatRepository: GeminiLocalChatRepository

    @Before
    fun setup() {
        messageDao = mock(TestMessageDao::class.java)
        chatDao = TestChatDao()
        geminiLocalChatRepository = GeminiLocalChatRepository(chatDao, messageDao)
    }

    @Test
    fun Gemini_test_all_local_chat() = testScope.runTest {
        assertEquals(
            geminiLocalChatRepository.allChats().first(),
            chatDao.getAllChats().first().map(ChatEntity::asExternalModel)
        )
    }

    @Test
    fun Gemini_test_messages_by_chat() = testScope.runTest {
        assertEquals(
            geminiLocalChatRepository.getMessagesByChat("chat_id"),
            chatDao.getChatWithMessages("chat_id").messages.map(AiMessageEntity::asExternalModel)
        )
    }

    @Test
    fun Gemini_test_save_chat() = testScope.runTest {
        geminiLocalChatRepository.saveChat(AiChat("chat_id_1", "chat_name", 888))
        assertEquals(
            geminiLocalChatRepository.allChats().first().size,
            2
        )
    }

    @Test
    fun Gemini_test_save_message() = testScope.runTest {
        val message = ChatMessage()
        val messageEntity = message.asEntity("chat_id_1")
        geminiLocalChatRepository.saveMessage("chat_id_1", message)
        verify(messageDao, times(1)).insert(messageEntity)
    }
}