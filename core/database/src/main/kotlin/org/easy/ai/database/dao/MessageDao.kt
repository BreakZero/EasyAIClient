package org.easy.ai.database.dao

import androidx.room.Dao
import org.easy.ai.database.entities.MessageEntity

@Dao
interface MessageDao: BaseDao<MessageEntity>