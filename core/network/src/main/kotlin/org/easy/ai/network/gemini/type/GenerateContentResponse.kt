package org.easy.ai.network.gemini.type

import android.util.Log

class GenerateContentResponse(
    val candidates: List<Candidate>,
    val promptFeedback: PromptFeedback?
) {
    /** Convenience field representing the first text part in the response, if it exists. */
    val text: String? by lazy { firstPartAs<TextPart>()?.text }

    private inline fun <reified T : Part> firstPartAs(): T? {
        if (candidates.isEmpty()) {
            warn("No candidates were found, but was asked to get a candidate.")
            return null
        }

        val (parts, otherParts) = candidates.first().content.parts.partition { it is T }
        val type = T::class.simpleName ?: "of the part type you asked for"

        if (parts.isEmpty()) {
            if (otherParts.isNotEmpty()) {
                warn(
                    "We didn't find any $type, but we did find other part types. Did you ask for the right type?"
                )
            }

            return null
        }

        if (parts.size > 1) {
            warn("Multiple $type were found, returning the first one.")
        } else if (otherParts.isNotEmpty()) {
            warn("Returning the only $type found, but other part types were present as well.")
        }

        return parts.first() as T
    }

    private fun warn(message: String) {
        Log.w("GenerateContentResponse", message)
    }
}