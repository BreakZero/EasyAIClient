package org.easy.ai.network.gemini

import kotlinx.coroutines.flow.Flow
import org.easy.ai.network.gemini.type.Content
import org.easy.ai.network.gemini.type.GenerateContentResponse

interface GeminiRestApi {
    suspend fun generateContent(
        apiKey: String,
        model: String,
        vararg content: Content
    ): GenerateContentResponse

    fun generateContentStream(
        apiKey: String,
        model: String,
        vararg content: Content
    ): Flow<GenerateContentResponse>
}