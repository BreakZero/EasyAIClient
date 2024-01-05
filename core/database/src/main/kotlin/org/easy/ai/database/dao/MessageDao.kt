package org.easy.ai.database.dao

import androidx.room.Dao
import org.easy.ai.database.entities.AiMessageEntity

@Dao
interface MessageDao: BaseDao<AiMessageEntity>