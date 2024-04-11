package org.easy.ai.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.ChatHistory
import org.easy.ai.database.entities.ChatWithMessage

@Dao
interface ChatDao : BaseDao<ChatEntity> {
    @Query("SELECT * FROM TB_CHAT")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Transaction
    @Query("SELECT * FROM TB_CHAT WHERE CHAT_ID = :chatId")
    suspend fun getChatWithMessages(chatId: String): ChatWithMessage

    @Transaction
    @Query("select * from tb_chat where chat_id = :chatId")
    suspend fun getChatHistoryByChatId(chatId: String): ChatHistory
}