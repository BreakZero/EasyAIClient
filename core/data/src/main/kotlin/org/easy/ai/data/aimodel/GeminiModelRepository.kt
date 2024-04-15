package org.easy.ai.data.aimodel

import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.gemini.type.content
import javax.inject.Inject

class GeminiModelRepository @Inject internal constructor(
    private val geminiRestApi: GeminiRestApi
) : ChatPlugin {
    override suspend fun sendMessage(apiKey: String, history: List<ChatMessageContent>): ChatMessageContent {
        val content = history.map {
            content(it.participant.name.lowercase()) { text(it.message) }
        }
        val response = geminiRestApi.generateContent(apiKey, *content.toTypedArray())
        return ChatMessageContent(message = response.text.orEmpty(), participant = Participant.MODEL)
    }
}