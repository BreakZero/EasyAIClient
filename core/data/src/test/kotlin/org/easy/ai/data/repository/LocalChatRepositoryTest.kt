package org.easy.ai.data.repository

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.data.model.asUiModel
import org.easy.ai.data.testdoubles.TestChatDao
import org.easy.ai.data.testdoubles.TestMessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ChatMessageUiModel
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


class LocalChatRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var messageDao: TestMessageDao
    private lateinit var chatDao: TestChatDao
    private lateinit var localChatRepository: LocalChatRepository

    @Before
    fun setup() {
        messageDao = mock(TestMessageDao::class.java)
        chatDao = TestChatDao()
        localChatRepository = LocalChatRepository(chatDao, messageDao)
    }

    @Test
    fun Gemini_test_all_local_chat() = testScope.runTest {
        assertEquals(
            localChatRepository.getAllChats().first(),
            chatDao.getAllChats().first().map(ChatEntity::asExternalModel)
        )
    }

//    @Test
//    fun Gemini_test_messages_by_chat() = testScope.runTest {
//        assertEquals(
//            localChatRepository.getMessagesByChat("chat_id").also {
//                println("=== $it")
//            },
//            messageDao.getChatHistoryByChatId("chat_id").messages.map(MessageEntity::asUiModel)
//        )
//    }

    @Test
    fun Gemini_test_save_chat() = testScope.runTest {
        localChatRepository.saveChat(
            chatId = "chat_id",
            name = "Mock Chat Name",
            platform = ModelPlatform.GEMINI
        )
        assertEquals(
            localChatRepository.getAllChats().first().size,
            2
        )
    }

    @Test
    fun Gemini_test_save_message() = testScope.runTest {
        val message =
            ChatMessageUiModel(text = "content", participant = Participant.USER, isPending = true)
        val messageEntity = MessageEntity(
            participant = Participant.USER,
            content = "content",
            chatId = "chat_id_1",
            timestamp = System.currentTimeMillis()
        )
        localChatRepository.saveMessage("chat_id_1", "content", participant = Participant.USER)
        verify(messageDao, times(1)).insert(messageEntity)
    }
}