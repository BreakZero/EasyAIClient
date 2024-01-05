package org.easy.ai.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.easy.ai.model.Participant

data class ChatWithMessage(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "belong_to"
    )
    val messages: List<AiMessageEntity> = ArrayList<AiMessageEntity>()
)

@Entity(tableName = "tb_chat_message")
data class AiMessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val id: String,
    val participant: Participant,
    val text: String? = null,
    @ColumnInfo(name = "belong_to")
    val belong: String,
    val timestamp: Long
)
