package org.easy.ai.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.easy.ai.database.entities.ChatHistory
import org.easy.ai.database.entities.MessageEntity

@Dao
interface MessageDao : BaseDao<MessageEntity> {
    @Transaction
    @Query("select * from tb_chat where chat_id = :chatId")
    suspend fun getChatHistoryByChatId(chatId: String): ChatHistory
}