package org.easy.ai.network.gemini.internal.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenerationConfig(
    val temperature: Float?,
    @SerialName("top_p") val topP: Float?,
    @SerialName("top_k") val topK: Int?,
    @SerialName("candidate_count") val candidateCount: Int?,
    @SerialName("max_output_tokens") val maxOutputTokens: Int?,
    @SerialName("stop_sequences") val stopSequences: List<String>?
)