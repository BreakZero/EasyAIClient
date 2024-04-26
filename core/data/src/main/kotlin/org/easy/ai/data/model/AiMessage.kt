package org.easy.ai.data.model

import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.MessageType

fun MessageEntity.asExternalModel(): ChatMessage {
    return ChatMessage(
        content = content,
        participant = participant,
        type = MessageType.SUCCESS
    )
}
