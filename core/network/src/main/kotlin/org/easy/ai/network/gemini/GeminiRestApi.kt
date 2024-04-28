package org.easy.ai.network.gemini

import kotlinx.coroutines.flow.Flow
import org.easy.ai.network.model.Content
import org.easy.ai.network.model.PromptResponse

interface GeminiRestApi {
    suspend fun generateContent(
        apiKey: String,
        model: String,
        vararg content: Content
    ): PromptResponse

    fun generateContentStream(
        apiKey: String,
        model: String,
        vararg content: Content
    ): Flow<PromptResponse>
}