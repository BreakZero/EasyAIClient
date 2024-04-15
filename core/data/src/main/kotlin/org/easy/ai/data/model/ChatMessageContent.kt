package org.easy.ai.data.model

import org.easy.ai.model.Participant

data class ChatMessageContent(
    val message: String,
    val participant: Participant,
)
