package org.easy.ai.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.easy.ai.model.Participant

@Entity(
    tableName = "tb_message",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = arrayOf("chat_id"),
            childColumns = arrayOf("chat_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "msg_id")
    val messageId: Long? = null,
    @ColumnInfo(name = "participant")
    val participant: Participant,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "chat_id", index = true)
    val chatId: String,
    @ColumnInfo(name = "create_at")
    val timestamp: Long
)

data class ChatHistory(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "chat_id"
    )
    val messages: List<MessageEntity> = emptyList()
)