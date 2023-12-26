package org.easy.ai.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class ChatHistoryWithMessage(
    @Embedded
    val chatHistoryEntity: ChatHistoryEntity,
    @Relation(
        parentColumn = "history_id",
        entityColumn = "belong_to"
    )
    val messages: List<MessageEntity> = ArrayList<MessageEntity>()
)

@Entity(tableName = "tb_chat_history")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    val id: Long,
    @ColumnInfo(name = "chat_id")
    val chatId: Long,
    val role: String
)

@Entity(tableName = "tb_chat_message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id")
    val id: Long,
    val part: MessagePart,
    val content: ByteArray,
    @ColumnInfo(name = "belong_to")
    val belong: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageEntity

        if (id != other.id) return false
        if (part != other.part) return false
        if (!content.contentEquals(other.content)) return false
        return belong == other.belong
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + part.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + belong.hashCode()
        return result
    }
}

enum class MessagePart {
    TEXT, IMAGE, BLOB
}
