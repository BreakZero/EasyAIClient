package org.easy.ai.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_chat")
data class ChatEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chat_id")
    val chatId: Long,
    @ColumnInfo(name = "chat_name")
    val chatName: String,
    @ColumnInfo(name = "create_at")
    val createAt: Long
)
