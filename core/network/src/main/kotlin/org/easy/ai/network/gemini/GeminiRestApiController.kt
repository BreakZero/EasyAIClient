package org.easy.ai.network.gemini

import android.accounts.NetworkErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import org.easy.ai.network.di.JSON
import org.easy.ai.network.gemini.internal.CountTokensRequest
import org.easy.ai.network.gemini.internal.GRpcErrorResponse
import org.easy.ai.network.gemini.internal.GenerateContentRequest
import org.easy.ai.network.gemini.internal.GenerateContentResponse
import org.easy.ai.network.gemini.internal.Request
import org.easy.ai.network.gemini.type.Content
import org.easy.ai.network.gemini.type.Content as PublicContent
import org.easy.ai.network.gemini.type.FinishReason
import org.easy.ai.network.gemini.type.PromptBlockedException
import org.easy.ai.network.gemini.type.ResponseStoppedException
import org.easy.ai.network.gemini.util.toInternal
import org.easy.ai.network.gemini.util.toPublic

class GeminiRestApiController internal constructor(
    private val httpClient: HttpClient
) : GeminiRestApi {
    override suspend fun generateContent(
        apiKey: String,
        vararg content: PublicContent
    ): org.easy.ai.network.gemini.type.GenerateContentResponse {
        val request = constructRequest(*content)
        val response =
            httpClient.post("models/gemini-pro:generateContent") {
                applyCommonConfiguration(apiKey, request)
            }.also {
                validateResponse(it)
            }.body<GenerateContentResponse>()

        return response.toPublic().validate()
    }

    override suspend fun generateContentByVision(
        apiKey: String,
        vararg content: Content
    ): org.easy.ai.network.gemini.type.GenerateContentResponse {
        val request = constructRequest(*content)
        val response =
            httpClient.post("models/gemini-pro-vision:generateContent") {
                applyCommonConfiguration(apiKey, request)
            }.also {
                validateResponse(it)
            }.body<GenerateContentResponse>()

        return response.toPublic().validate()
    }

    private fun HttpRequestBuilder.applyCommonConfiguration(apiKey: String, request: Request) {
        when (request) {
            is GenerateContentRequest -> setBody<GenerateContentRequest>(request)
            is CountTokensRequest -> setBody<CountTokensRequest>(request)
        }
        header("x-goog-api-key", apiKey)
    }

    private fun constructRequest(vararg content: PublicContent): GenerateContentRequest {
        return GenerateContentRequest(
            contents = content.map(PublicContent::toInternal)
        )
    }
}

private suspend fun validateResponse(response: HttpResponse) {
    if (response.status != HttpStatusCode.OK) {
        val text = response.bodyAsText()
        val message =
            try {
                JSON.decodeFromString<GRpcErrorResponse>(text).error.message
            } catch (e: Throwable) {
                "Unexpected Response:\n$text"
            }

        throw NetworkErrorException(message)
    }
}

private fun org.easy.ai.network.gemini.type.GenerateContentResponse.validate() = apply {
    if (candidates.isEmpty() && promptFeedback == null) {
        throw SerializationException("Error deserializing response, found no valid fields")
    }
    promptFeedback?.blockReason?.let { throw PromptBlockedException(this) }
    candidates
        .mapNotNull { it.finishReason }
        .firstOrNull { it != FinishReason.STOP }
        ?.let { throw ResponseStoppedException(this) }
}