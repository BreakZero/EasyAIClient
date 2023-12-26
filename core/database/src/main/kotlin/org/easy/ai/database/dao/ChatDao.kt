package org.easy.ai.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.easy.ai.database.entities.ChatEntity

@Dao
abstract class ChatDao: BaseDao<ChatEntity> {
    @Query("SELECT * FROM TB_CHAT")
    abstract suspend fun getAllChats(): List<ChatEntity>
}