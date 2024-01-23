package org.easy.ai.data.model

import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel

interface AiModel

data class GeminiAiModel(
    val model: GenerativeModel,
    val chat: Chat
): AiModel

data class ChatGPTAiModel(
    val model: String
): AiModel
