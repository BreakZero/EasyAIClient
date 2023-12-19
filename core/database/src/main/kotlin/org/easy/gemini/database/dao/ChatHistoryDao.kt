package org.easy.gemini.database.dao

import androidx.room.Dao
import org.easy.gemini.data.entities.ChatHistoryEntity

@Dao
abstract class ChatHistoryDao: BaseDao<ChatHistoryEntity> {

}