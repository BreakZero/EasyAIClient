package org.easy.ai.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.Participant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AiModelResourceDaoTest {

    private lateinit var db: EasyAIDatabase
    private lateinit var chatDao: ChatDao
    private lateinit var messageDao: MessageDao

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, EasyAIDatabase::class.java).build()
        chatDao = db.chatDao()
        messageDao = db.messageDao()
    }

    @Test
    fun Test_chat_fetches_items() = runTest {
        val chatEntities = (1..5).map {
            newChatEntity(it)
        }
        chatDao.insert(*chatEntities.toTypedArray())
        val savedChats = chatDao.getAllChats().first()
        assertEquals(savedChats.size, 5)
        assertEquals(
            listOf("_id1", "_id2", "_id3", "_id4", "_id5"),
            savedChats.map { it.chatId }
        )
    }

    @Test
    fun Test_saved_chat_with_message() = runTest {
        val chatEntity = newChatEntity(1)
        val aiMessage = AiMessageEntity(
            id = "_mid",
            participant = Participant.MODEL,
            belong = "_id1",
            timestamp = 0L
        )
        chatDao.insert(chatEntity)
        messageDao.insert(aiMessage)

        val chatWithMessage = chatDao.getChatWithMessages("_id1")
        assertEquals(chatWithMessage.chat, chatEntity)
        assertEquals(chatWithMessage.messages, listOf(aiMessage))
    }
}

private fun newChatEntity(id: Int): ChatEntity {
    return ChatEntity(chatId = "_id$id", chatName = "_name$id", createAt = 1L)
}