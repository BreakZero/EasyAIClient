package org.easy.ai.database.dao

import androidx.room.Dao
import org.easy.ai.data.entities.ChatHistoryEntity

@Dao
abstract class ChatHistoryDao: BaseDao<ChatHistoryEntity> {

}