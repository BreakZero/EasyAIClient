package org.easy.ai.network.gemini.type

class PromptFeedback(
    val blockReason: BlockReason?,
    val safetyRatings: List<SafetyRating>
)

/** Describes why content was blocked. */
enum class BlockReason {
    /** A new and not yet supported value. */
    UNKNOWN,

    /** Content was blocked for an unspecified reason. */
    UNSPECIFIED,

    /** Content was blocked for violating provided [SafetySetting]s. */
    SAFETY,

    /** Content was blocked for another reason. */
    OTHER
}