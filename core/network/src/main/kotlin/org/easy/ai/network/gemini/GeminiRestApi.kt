package org.easy.ai.network.gemini

import org.easy.ai.model.EasyPrompt

interface GeminiRestApi {
    suspend fun generateContent(apiKey: String, prompt: EasyPrompt): String
}