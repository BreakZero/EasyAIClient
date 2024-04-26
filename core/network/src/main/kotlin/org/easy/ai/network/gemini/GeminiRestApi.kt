package org.easy.ai.network.gemini

import org.easy.ai.network.gemini.type.Content
import org.easy.ai.network.gemini.type.GenerateContentResponse

interface GeminiRestApi {
    suspend fun generateContent(apiKey: String, vararg content: Content): GenerateContentResponse

    suspend fun generateContentByVision(
        apiKey: String,
        vararg content: Content
    ): GenerateContentResponse
}