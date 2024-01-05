package org.easy.ai.data.model

import org.easy.ai.database.entities.AiMessageEntity

data class AiMessage(
    val id: String,
    val text: String? = null,
    val timestamp: Long
)

fun AiMessage.asEntity(chatId: String): AiMessageEntity {
    return AiMessageEntity(id, text, chatId, timestamp)
}

fun AiMessageEntity.asExternalModel(): AiMessage {
    return AiMessage(id, text, timestamp)
}
