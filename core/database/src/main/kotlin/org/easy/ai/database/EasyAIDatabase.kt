package org.easy.ai.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.easy.ai.database.converts.EnumConverts
import org.easy.ai.database.dao.ChatDao
import org.easy.ai.database.dao.MessageDao
import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.database.migration.AutoMigration4To5

@Database(
    entities = [ChatEntity::class, MessageEntity::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 4, to = 5, spec = AutoMigration4To5::class)
    ],
    exportSchema = true
)

@TypeConverters(EnumConverts::class)
abstract class EasyAIDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}