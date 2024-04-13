package org.easy.ai.network.gemini

import kotlinx.serialization.Serializable
import org.easy.ai.network.gemini.server.Candidate
import org.easy.ai.network.gemini.server.GRpcError
import org.easy.ai.network.gemini.server.PromptFeedback

internal sealed interface Response

@Serializable
internal data class GenerateContentResponse(
    val candidates: List<Candidate>? = null,
    val promptFeedback: PromptFeedback? = null,
) : Response

@Serializable internal data class CountTokensResponse(val totalTokens: Int) : Response

@Serializable internal data class GRpcErrorResponse(val error: GRpcError) : Response