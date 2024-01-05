package org.easy.ai.data.model

import org.easy.ai.database.entities.ChatEntity

data class AiChat(
    val chatId: String,
    val name: String,
    val createAt: Long
)

fun AiChat.asEntity(): ChatEntity {
    return ChatEntity(this.chatId, this.name, this.createAt)
}

fun ChatEntity.asExternalModel(): AiChat {
    return AiChat(this.chatId, this.chatName, this.createAt)
}
