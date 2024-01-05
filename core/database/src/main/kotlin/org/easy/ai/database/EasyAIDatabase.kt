package org.easy.ai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.AiMessageEntity

@Database(
    entities = [ChatEntity::class, AiMessageEntity::class],
    version = 1,
    exportSchema = true
)
abstract class EasyAIDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}