package org.easy.ai.data.model

import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.model.ChatMessage

fun ChatMessage.asEntity(chatId: String): AiMessageEntity {
    return AiMessageEntity(
        id = id,
        text = text,
        participant = participant,
        belong = chatId,
        timestamp = System.currentTimeMillis()
    )
}

fun AiMessageEntity.asExternalModel(): ChatMessage {
    return ChatMessage(
        id = id,
        text = text.orEmpty(),
        participant = participant,
        isPending = false
    )
}
