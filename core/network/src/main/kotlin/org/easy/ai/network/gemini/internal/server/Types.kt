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
    @SerialName("blockReason")
    val blockReason: BlockReason? = null,
    @SerialName("safetyRatings")
    val safetyRatings: List<SafetyRating>? = null,
)

@Serializable(BlockReasonSerializer::class)
internal enum class BlockReason {
    UNKNOWN,
    @SerialName("BLOCKED_REASON_UNSPECIFIED")
    UNSPECIFIED,
    SAFETY,
    OTHER
}

@Serializable
internal data class Candidate(
    @SerialName("content")
    val content: Content? = null,
    @SerialName("finishReason")
    val finishReason: FinishReason? = null,
    @SerialName("safetyRatings")
    val safetyRatings: List<SafetyRating>? = null,
    @SerialName("citationMetadata")
    val citationMetadata: CitationMetadata? = null
)

@Serializable
internal data class CitationMetadata(
    @SerialName("citationSources")
    val citationSources: List<CitationSources>
)

@Serializable
internal data class CitationSources(
    @SerialName("startIndex")
    val startIndex: Int,
    @SerialName("endIndex")
    val endIndex: Int,
    @SerialName("uri")
    val uri: String,
    @SerialName("license")
    val license: String
)

@Serializable
internal data class SafetyRating(
    @SerialName("category")
    val category: HarmCategory,
    @SerialName("probability")
    val probability: HarmProbability,
    @SerialName("blocked")
    val blocked: Boolean? = null // TODO(): any reason not to default to false?
)

@Serializable(HarmProbabilitySerializer::class)
internal enum class HarmProbability {
    @SerialName("UNKNOWN")
    UNKNOWN,
    @SerialName("HARM_PROBABILITY_UNSPECIFIED")
    UNSPECIFIED,
    @SerialName("NEGLIGIBLE")
    NEGLIGIBLE,
    @SerialName("LOW")
    LOW,
    @SerialName("MEDIUM")
    MEDIUM,
    @SerialName("HIGH")
    HIGH
}

@Serializable(FinishReasonSerializer::class)
internal enum class FinishReason {
//    @SerialName("UNKNOWN")
//    UNKNOWN,
    @SerialName("FINISH_REASON_UNSPECIFIED")
    UNSPECIFIED,
    @SerialName("STOP")
    STOP,
    @SerialName("MAX_TOKENS")
    MAX_TOKENS,
    @SerialName("SAFETY")
    SAFETY,
    @SerialName("RECITATION")
    RECITATION,
    @SerialName("OTHER")
    OTHER
}

@Serializable
internal data class GRpcError(
    val code: Int,
    val message: String,
)
