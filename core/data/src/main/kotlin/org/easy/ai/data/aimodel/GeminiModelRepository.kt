package org.easy.ai.data.aimodel

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.data.plugins.TextAndImagePlugin
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.gemini.type.content

class GeminiModelRepository @Inject internal constructor(
    private val geminiRestApi: GeminiRestApi
) : ChatPlugin, TextAndImagePlugin {
    override suspend fun sendMessage(apiKey: String, history: List<ChatMessage>): ChatMessage {
        val content = history.map {
            content(it.participant.name.lowercase()) { text(it.content) }
        }
        val response = geminiRestApi.generateContent(apiKey, "gemini-pro", *content.toTypedArray())
        return ChatMessage(
            content = response.text.orEmpty(),
            participant = Participant.MODEL
        )
    }

    override fun generateContentStream(
        apiKey: String,
        prompt: String,
        images: List<ByteArray>
    ): Flow<String> {
        val bitmaps = images.map {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                .asAndroidBitmap()
        }
        val content = content {
            text(prompt)
            bitmaps.map(::image)
        }
        val response = geminiRestApi.generateContentStream(apiKey = apiKey, "gemini-pro-vision", content)
        bitmaps.forEach { it.recycle() }
        return response.map { it.text.orEmpty() }
    }
}