package org.easy.ai.data.repository

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.easy.ai.data.model.asExternalModel
import org.easy.ai.data.testdoubles.DataHolder
import org.easy.ai.data.testdoubles.TestChatDao
import org.easy.ai.data.testdoubles.TestMessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.Participant
import org.junit.After
import org.junit.Before
import org.junit.Test


class LocalChatRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var messageDao: TestMessageDao
    private lateinit var chatDao: TestChatDao
    private lateinit var localChatRepository: LocalChatRepository

    private val mockChat = ChatEntity("chat_id", "chat_name", ModelPlatform.GEMINI, 888)
    private val mockMessage = MessageEntity(
        messageId = 0L,
        participant = Participant.USER,
        content = "mock message content",
        chatId = "chat_id",
        timestamp = 999
    )

    @Before
    fun setup() {
        messageDao = TestMessageDao()
        chatDao = TestChatDao()
        localChatRepository = LocalChatRepository(chatDao, messageDao)
    }

    @After
    fun clear() {
        DataHolder.chatTable.clear()
        DataHolder.messageTable.clear()
    }

    @Test
    fun test_chat_insert() = testScope.runTest {
        chatDao.insert(mockChat)
        assertEquals(1, localChatRepository.getAllChats().first().size)
        assertEquals(mockChat.asExternalModel(), localChatRepository.getChatById(mockChat.chatId))
    }

    @Test
    fun test_insert_message() = testScope.runTest {
        chatDao.insert(mockChat)
        messageDao.insert(mockMessage)
        assertEquals(
            1,
            localChatRepository.getMessagesByChat(mockChat.chatId).size
        )
    }

    @Test
    fun test_delete_chat() = testScope.runTest {
        localChatRepository.deleteChatById(mockChat.chatId)
        assertEquals(
            0,
            localChatRepository.getAllChats().first().size
        )
    }

    @Test
    fun test_save_chat() = testScope.runTest {
        localChatRepository.saveChat(
            chatId = "saving-chat",
            name = "saving_chat",
            platform = ModelPlatform.GEMINI
        )
        assertEquals(
            1,
            localChatRepository.getAllChats().first().size
        )
    }

    @Test
    fun test_save_chat_null() = testScope.runTest {
        localChatRepository.saveChat(
            chatId = null,
            name = "saving_chat",
            platform = ModelPlatform.GEMINI
        )
        assertEquals(
            "saving_chat",
            localChatRepository.getAllChats().first().first().name
        )
    }

    @Test
    fun test_save_message() = testScope.runTest {
        localChatRepository.saveChat(
            chatId = "saving-chat",
            name = "saving_chat",
            platform = ModelPlatform.GEMINI
        )
        localChatRepository.saveMessage(
            "saving-chat",
            text = "message content",
            participant = Participant.MODEL
        )
        assertEquals(
            localChatRepository.getMessagesByChat("saving-chat").first().text,
            "message content"
        )
    }
}