package org.easy.ai.network.gemini.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.easy.ai.network.gemini.internal.client.GenerationConfig
import org.easy.ai.network.gemini.internal.shared.Content
import org.easy.ai.network.gemini.internal.shared.SafetySetting

internal sealed interface Request

@Serializable
internal data class GenerateContentRequest(
    val contents: List<Content>,
    @SerialName("safety_settings") val safetySettings: List<SafetySetting>? = null,
    @SerialName("generation_config") val generationConfig: GenerationConfig? = null
) : Request

@Serializable
internal data class CountTokensRequest(val contents: List<Content>) : Request