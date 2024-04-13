package org.easy.ai.model

enum class Participant {
    USER, MODEL, ERROR
}
data class ChatMessageUiModel(
    val text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = false
)
