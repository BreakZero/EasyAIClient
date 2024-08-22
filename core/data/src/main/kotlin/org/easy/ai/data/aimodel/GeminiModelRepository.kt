package org.easy.ai.data.aimodel

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.data.plugins.TextAndImagePlugin
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.model.content
import javax.inject.Inject

class GeminiModelRepository @Inject internal constructor(
    private val geminiRestApi: GeminiRestApi
) : ChatPlugin, TextAndImagePlugin {
    private val defaultModel = "gemini-1.5-pro"
    override suspend fun sendMessage(apiKey: String, history: List<ChatMessage>): ChatMessage {
        val content = history.map {
            content(it.participant.name.lowercase()) { text(it.content) }
        }
        val response = geminiRestApi.generateContent(apiKey, defaultModel, *content.toTypedArray())
        return ChatMessage(
            content = response.text.orEmpty(),
            participant = Participant.MODEL
        )
    }

    override fun sendMessageStream(apiKey: String, history: List<ChatMessage>): Flow<ChatMessage> {
        val content = history.map {
            content(it.participant.name.lowercase()) { text(it.content) }
        }
        val responseStream = geminiRestApi.generateContentStream(
            apiKey,
            defaultModel,
            *content.toTypedArray()
        )
        return responseStream.map {
            ChatMessage.success(
                content = it.text.orEmpty(),
                participant = Participant.MODEL
            )
        }
    }

    override fun generateContentStream(
        apiKey: String,
        prompt: String,
        images: List<Bitmap>?
    ): Flow<String> {
        val content = images?.let {
            content {
                text(prompt)
                images.map(::image)
            }
        } ?: content {
            text(prompt)
        }

        val response = geminiRestApi.generateContentStream(apiKey = apiKey, defaultModel, content)

        return response.map { it.text.orEmpty() }
    }
}