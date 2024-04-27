package org.easy.ai.network.error

import org.easy.ai.network.gemini.internal.GenerateContentResponse

class PromptBlockedException internal constructor(
    response: GenerateContentResponse,
    cause: Throwable? = null
) :
    RuntimeException(
        "Prompt was blocked: ${response.promptFeedback?.blockReason?.name}",
        cause
    )

class ResponseStoppedException internal constructor(
    response: GenerateContentResponse,
    cause: Throwable? = null
) : RuntimeException(
    "Content generation stopped. Reason: ${response.candidates?.first()?.finishReason?.name}",
    cause
)