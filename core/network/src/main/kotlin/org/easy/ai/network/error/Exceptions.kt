package org.easy.ai.network.error

import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.TimeoutCancellationException
import org.easy.ai.network.gemini.internal.GenerateContentResponse

sealed class GoogleGenerativeAIException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause) {
    companion object {

        /**
         * Converts a [Throwable] to a [GoogleGenerativeAIException].
         *
         * Will populate default messages as expected, and propagate the provided [cause] through the
         * resulting exception.
         */
        fun from(cause: Throwable): GoogleGenerativeAIException =
            when (cause) {
                is GoogleGenerativeAIException -> cause
                is JsonConvertException,
                is kotlinx.serialization.SerializationException -> SerializationException(
                    "Something went wrong while trying to deserialize a response from the server.",
                    cause,
                )

                is TimeoutCancellationException ->
                    RequestTimeoutException("The request failed to complete in the allotted time.")

                else -> UnknownException("Something unexpected happened.", cause)
            }
    }
}

class ServerException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)

class InvalidAPIKeyException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)

class UnsupportedUserLocationException(cause: Throwable? = null) :
    GoogleGenerativeAIException("User location is not supported for the API use.", cause)


class PromptBlockedException internal constructor(
    response: GenerateContentResponse,
    cause: Throwable? = null
) : GoogleGenerativeAIException(
    "Prompt was blocked: ${response.promptFeedback?.blockReason?.name}",
    cause
)

class ResponseStoppedException internal constructor(
    response: GenerateContentResponse,
    cause: Throwable? = null
) : GoogleGenerativeAIException(
    "Content generation stopped. Reason: ${response.candidates?.first()?.finishReason?.name}",
    cause
)

class SerializationException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)

class RequestTimeoutException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)

class QuotaExceededException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)

class UnknownException(message: String, cause: Throwable? = null) :
    GoogleGenerativeAIException(message, cause)