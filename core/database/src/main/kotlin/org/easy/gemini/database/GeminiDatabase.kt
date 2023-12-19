package org.easy.gemini.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.easy.gemini.database.dao.ChatDao
import org.easy.gemini.database.dao.ChatHistoryDao
import org.easy.gemini.database.dao.MessageDao
import org.easy.gemini.database.entities.ChatEntity
import org.easy.gemini.database.entities.ChatHistoryEntity
import org.easy.gemini.database.entities.MessageEntity

@Database(
    entities = [ChatEntity::class, ChatHistoryEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GeminiDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun chatHistoryDao(): ChatHistoryDao
    abstract fun messageDao(): MessageDao
}