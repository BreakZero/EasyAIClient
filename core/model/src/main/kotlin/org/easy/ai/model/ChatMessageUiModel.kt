package org.easy.ai.model

enum class Participant {
    USER,
    MODEL
}

enum class MessageType {
    ERROR,
    PENDING,
    SUCCESS
}

data class ChatMessage(
    val content: String,
    val participant: Participant = Participant.USER,
    val type: MessageType = MessageType.PENDING
) {
    companion object {
        fun error(error: Throwable): ChatMessage {
            return ChatMessage(
                content = error.message ?: "unknown error",
                type = MessageType.ERROR,
                participant = Participant.MODEL
            )
        }

        fun success(
            content: String,
            participant: Participant
        ): ChatMessage {
            return ChatMessage(content = content, participant = participant, type = MessageType.SUCCESS)
        }
    }

    fun isSuccess(): Boolean {
        return type == MessageType.SUCCESS
    }

    fun isError(): Boolean {
        return type == MessageType.ERROR
    }
}
