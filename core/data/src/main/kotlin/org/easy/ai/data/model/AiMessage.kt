package org.easy.ai.data.model

import org.easy.ai.database.entities.MessageEntity
import org.easy.ai.model.ChatMessageUiModel

fun MessageEntity.asUiModel(): ChatMessageUiModel {
    return ChatMessageUiModel(
        text = content,
        participant = participant,
        isPending = false
    )
}
