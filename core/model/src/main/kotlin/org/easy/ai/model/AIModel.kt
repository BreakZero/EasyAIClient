package org.easy.ai.model

enum class AIModel(
    val model: String,
    val maxToken: Int
) {
    GPT35Turbo("", 4096),
    GeminiPro("gemini-pro", 0),
    GeminiProVision("gemini-pro-vision", 0)
}