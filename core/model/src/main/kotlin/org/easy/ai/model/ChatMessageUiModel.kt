package org.easy.ai.model

enum class Participant {
    USER, MODEL
}

enum class MessageType {
    ERROR, PENDING, SUCCESS
}

data class ChatMessage(
    val content: String,
    val participant: Participant = Participant.USER,
    val type: MessageType = MessageType.PENDING
)
