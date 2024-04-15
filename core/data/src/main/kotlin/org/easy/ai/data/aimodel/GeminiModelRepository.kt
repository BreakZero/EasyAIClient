package org.easy.ai.data.aimodel

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.easy.ai.data.model.ChatMessageContent
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.data.plugins.MultiModalPlugin
import org.easy.ai.model.Participant
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.gemini.type.content
import javax.inject.Inject

class GeminiModelRepository @Inject internal constructor(
    private val geminiRestApi: GeminiRestApi
) : ChatPlugin, MultiModalPlugin {
    override suspend fun sendMessage(
        apiKey: String,
        history: List<ChatMessageContent>
    ): ChatMessageContent {
        val content = history.map {
            content(it.participant.name.lowercase()) { text(it.message) }
        }
        val response = geminiRestApi.generateContent(apiKey, *content.toTypedArray())
        return ChatMessageContent(
            message = response.text.orEmpty(),
            participant = Participant.MODEL
        )
    }

    override suspend fun generateContent(
        apiKey: String,
        prompt: String,
        images: List<ByteArray>
    ): String {
        val bitmaps = images.map {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                .asAndroidBitmap()
        }
        val content = content {
            text(prompt)
            bitmaps.map(::image)
        }
        val response = geminiRestApi.generateContentByVision(apiKey = apiKey, content)
        bitmaps.forEach { it.recycle() }
        return response.text.orEmpty()
    }
}