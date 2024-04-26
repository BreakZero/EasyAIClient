package org.easy.ai.model

data class UserData(
    val apiKeys: Map<String, String>,
    val defaultChatModel: AiModel? = null,
    val isAutomaticSaveChat: Boolean
)