package org.easy.ai.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.easy.ai.database.entities.ChatEntity

@Dao
interface ChatDao : BaseDao<ChatEntity> {
    @Query("SELECT * FROM TB_CHAT")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("DELETE FROM TB_CHAT WHERE CHAT_ID = :chatId")
    suspend fun deleteChatById(chatId: String)
}