package org.easy.ai.network.gemini

import android.accounts.NetworkErrorException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import org.easy.ai.network.di.JSON
import org.easy.ai.network.error.PromptBlockedException
import org.easy.ai.network.error.ResponseStoppedException
import org.easy.ai.network.gemini.internal.CountTokensRequest
import org.easy.ai.network.gemini.internal.GRpcErrorResponse
import org.easy.ai.network.gemini.internal.GenerateContentRequest
import org.easy.ai.network.gemini.internal.GenerateContentResponse
import org.easy.ai.network.gemini.internal.Request
import org.easy.ai.network.gemini.internal.Response
import org.easy.ai.network.gemini.internal.server.FinishReason
import org.easy.ai.network.gemini.util.toInternal
import org.easy.ai.network.gemini.util.toResult
import org.easy.ai.network.model.PromptResponse
import org.easy.ai.network.util.decodeToFlow
import org.easy.ai.network.model.Content as PublicContent

class GeminiRestApiController internal constructor(
    private val httpClient: HttpClient
) : GeminiRestApi {
    override suspend fun generateContent(
        apiKey: String,
        model: String,
        vararg content: PublicContent
    ): PromptResponse {
        val request = constructRequest(*content)
        val response = httpClient.post("models/$model:generateContent") {
            applyCommonConfiguration(apiKey, request)
        }.also {
            validateResponse(it)
        }.body<GenerateContentResponse>().validate()

        return response.toResult()
    }

    override fun generateContentStream(
        apiKey: String,
        model: String,
        vararg content: PublicContent
    ): Flow<PromptResponse> {
        val request = constructRequest(*content)
        return httpClient.postStream<GenerateContentResponse>("models/$model:streamGenerateContent?alt=sse") {
            applyCommonConfiguration(apiKey, request)
        }.map {
            it.validate()
        }.map { it.toResult() }
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

private fun GenerateContentResponse.validate() = apply {
    if ((candidates?.isEmpty() != false) && promptFeedback == null) {
        throw SerializationException("Error deserializing response, found no valid fields")
    }
    promptFeedback?.blockReason?.let { throw PromptBlockedException(this) }
    candidates
        ?.mapNotNull { it.finishReason }
        ?.firstOrNull { it != FinishReason.STOP }
        ?.let { throw ResponseStoppedException(this) }
}

private inline fun <reified R : Response> HttpClient.postStream(
    url: String,
    crossinline config: HttpRequestBuilder.() -> Unit = {},
): Flow<R> = channelFlow {
    launch(CoroutineName("postStream")) {
        preparePost(url) {
            config()
        }.execute {
            validateResponse(it)

            val channel = it.bodyAsChannel()
            val flow = JSON.decodeToFlow<R>(channel)

            flow.collect { res -> send(res) }
        }
    }
}