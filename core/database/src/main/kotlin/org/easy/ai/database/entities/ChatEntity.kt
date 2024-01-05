package org.easy.ai.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_chat")
data class ChatEntity(
    @PrimaryKey
    @ColumnInfo(name = "chat_id")
    val chatId: String,
    @ColumnInfo(name = "chat_name")
    val chatName: String,
    @ColumnInfo(name = "create_at")
    val createAt: Long
)
