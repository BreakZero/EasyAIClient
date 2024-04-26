package org.easy.ai.network.gemini.type

class Candidate
internal constructor(
    val content: Content,
    val safetyRatings: List<SafetyRating>,
    val citationMetadata: List<CitationMetadata>,
    val finishReason: FinishReason?
)

enum class HarmCategory {
    /** A new and not yet supported value. */
    UNKNOWN,

    /** Harassment content. */
    HARASSMENT,

    /** Hate speech and content. */
    HATE_SPEECH,

    /** Sexually explicit content. */
    SEXUALLY_EXPLICIT,

    /** Dangerous content. */
    DANGEROUS_CONTENT
}

enum class HarmProbability {
    /** A new and not yet supported value. */
    UNKNOWN,

    /** Probability for harm is unspecified. */
    UNSPECIFIED,

    /** Probability for harm is negligible. */
    NEGLIGIBLE,

    /** Probability for harm is low. */
    LOW,

    /** Probability for harm is medium. */
    MEDIUM,

    /** Probability for harm is high. */
    HIGH
}

/** Rating for a particular [HarmCategory] with a provided [HarmProbability]. */
class SafetyRating(val category: HarmCategory, val probability: HarmProbability)

/**
 * Provides citation metadata for sourcing of content provided by the model between a given
 * [startIndex] and [endIndex].
 *
 * @property startIndex The beginning of the citation.
 * @property endIndex The end of the citation.
 * @property uri The URI of the cited work.
 * @property license The license under which the cited work is distributed.
 */
class CitationMetadata(
    val startIndex: Int,
    val endIndex: Int,
    val uri: String,
    val license: String
)

/** The reason for content finishing. */
enum class FinishReason {
    /** A new and not yet supported value. */
    UNKNOWN,

    /** Reason is unspecified. */
    UNSPECIFIED,

    /** Model finished successfully and stopped. */
    STOP,

    /** Model hit the token limit. */
    MAX_TOKENS,

    /** [SafetySetting]s prevented the model from outputting content. */
    SAFETY,

    /** Model began looping. */
    RECITATION,

    /** Model stopped for another reason. */
    OTHER
}