package org.easy.ai.network.gemini.type

class PromptBlockedException(val response: GenerateContentResponse, cause: Throwable? = null) :
    RuntimeException(
        "Prompt was blocked: ${response.promptFeedback?.blockReason?.name}",
        cause
    )

class ResponseStoppedException(val response: GenerateContentResponse, cause: Throwable? = null) :
    RuntimeException(
        "Content generation stopped. Reason: ${response.candidates.first().finishReason?.name}",
        cause
    )