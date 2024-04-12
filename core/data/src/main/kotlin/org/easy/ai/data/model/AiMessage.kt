package org.easy.ai.data.model

import org.easy.ai.database.entities.AiMessageEntity
import org.easy.ai.model.ChatMessageUiModel

fun AiMessageEntity.asExternalModel(): ChatMessageUiModel {
    return ChatMessageUiModel(
        text = text.orEmpty(),
        participant = participant,
        isPending = false
    )
}
