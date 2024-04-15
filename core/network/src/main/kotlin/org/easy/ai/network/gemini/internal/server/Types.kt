package org.easy.ai.network.gemini.internal.server

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.easy.ai.network.gemini.internal.shared.Content
import org.easy.ai.network.gemini.internal.shared.HarmCategory
import org.easy.ai.network.gemini.util.FirstOrdinalSerializer

internal object BlockReasonSerializer :
    KSerializer<BlockReason> by FirstOrdinalSerializer(BlockReason::class)

internal object HarmProbabilitySerializer :
    KSerializer<HarmProbability> by FirstOrdinalSerializer(HarmProbability::class)

internal object FinishReasonSerializer :
    KSerializer<FinishReason> by FirstOrdinalSerializer(FinishReason::class)

@Serializable
internal data class PromptFeedback(
    val blockReason: BlockReason? = null,
    val safetyRatings: List<SafetyRating>? = null,
)

@Serializable(BlockReasonSerializer::class)
internal enum class BlockReason {
    UNKNOWN,
    @SerialName("BLOCKED_REASON_UNSPECIFIED") UNSPECIFIED,
    SAFETY,
    OTHER
}

@Serializable
internal data class Candidate(
    val content: Content? = null,
    val finishReason: FinishReason? = null,
    val safetyRatings: List<SafetyRating>? = null,
    val citationMetadata: CitationMetadata? = null
)

@Serializable internal data class CitationMetadata(val citationSources: List<CitationSources>)

@Serializable
internal data class CitationSources(
    val startIndex: Int,
    val endIndex: Int,
    val uri: String,
    val license: String
)

@Serializable
internal data class SafetyRating(
    val category: HarmCategory,
    val probability: HarmProbability,
    val blocked: Boolean? = null // TODO(): any reason not to default to false?
)

@Serializable(HarmProbabilitySerializer::class)
internal enum class HarmProbability {
    UNKNOWN,
    @SerialName("HARM_PROBABILITY_UNSPECIFIED") UNSPECIFIED,
    NEGLIGIBLE,
    LOW,
    MEDIUM,
    HIGH
}

@Serializable(FinishReasonSerializer::class)
internal enum class FinishReason {
    UNKNOWN,
    @SerialName("FINISH_REASON_UNSPECIFIED") UNSPECIFIED,
    STOP,
    MAX_TOKENS,
    SAFETY,
    RECITATION,
    OTHER
}

@Serializable
internal data class GRpcError(
    val code: Int,
    val message: String,
)
