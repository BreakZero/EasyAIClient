package org.easy.ai.data.plugins

interface MultiModalPlugin {
    suspend fun generateContent(apiKey: String, prompt: String, images: List<ByteArray>): String
}