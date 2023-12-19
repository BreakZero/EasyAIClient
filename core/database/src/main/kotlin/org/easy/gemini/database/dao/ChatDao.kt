package org.easy.gemini.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.easy.gemini.database.entities.ChatEntity

@Dao
abstract class ChatDao: BaseDao<ChatEntity> {
    @Query("SELECT * FROM TB_CHAT")
    abstract suspend fun getAllChats(): List<ChatEntity>
}