package org.easy.ai.network.gemini.internal

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.easy.ai.network.gemini.internal.server.Candidate
import org.easy.ai.network.gemini.internal.server.GRpcError
import org.easy.ai.network.gemini.internal.server.PromptFeedback
import org.easy.ai.network.gemini.internal.shared.Part
import org.easy.ai.network.gemini.internal.shared.TextPart

internal sealed interface Response

@Serializable
internal data class GenerateContentResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>? = null,
    @SerialName("promptFeedback")
    val promptFeedback: PromptFeedback? = null
) : Response {
    val text: String? by lazy { firstPartAs<TextPart>()?.text }

    private inline fun <reified T : Part> firstPartAs(): T? {
        val candidates_ = candidates.orEmpty()
        if (candidates_.isEmpty()) {
            warn("No candidates were found, but was asked to get a candidate.")
            return null
        }

        val (parts, otherParts) = candidates_.first().content!!.parts.partition { it is T }
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

@Serializable internal data class CountTokensResponse(val totalTokens: Int) : Response

@Serializable internal data class GRpcErrorResponse(val error: GRpcError) : Response