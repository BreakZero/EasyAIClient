package org.easy.ai.data.model

import org.easy.ai.database.entities.ChatEntity
import org.easy.ai.model.ModelPlatform

data class AiChat(
    val chatId: String,
    val name: String,
    val createAt: Long
)

fun AiChat.asEntity(): ChatEntity {
    return ChatEntity(this.chatId, this.name, model = ModelPlatform.GEMINI, this.createAt)
}

fun ChatEntity.asExternalModel(): AiChat {
    return AiChat(this.chatId, this.chatName, this.createAt)
}
