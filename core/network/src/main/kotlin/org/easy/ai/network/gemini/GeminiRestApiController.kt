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
import org.easy.ai.model.EasyPrompt
import org.easy.ai.network.di.JSON
import org.easy.ai.network.gemini.shared.Content
import org.easy.ai.network.gemini.shared.TextPart

class GeminiRestApiController internal constructor(
    private val httpClient: HttpClient
) : GeminiRestApi {
    override suspend fun generateContent(apiKey: String, prompt: EasyPrompt): String {
        val request = constructRequest(prompt)
        val response = httpClient.post("gemini-pro:generateContent") {
            applyCommonConfiguration(apiKey, request)
        }.also { validateResponse(it) }
            .body<GenerateContentResponse>()
        println("===== $response")
        return ""
    }

    private fun HttpRequestBuilder.applyCommonConfiguration(apiKey: String, request: Request) {
        when (request) {
            is GenerateContentRequest -> setBody<GenerateContentRequest>(request)
            is CountTokensRequest -> setBody<CountTokensRequest>(request)
        }
        header("x-goog-api-key", apiKey)
    }

    private fun constructRequest(vararg prompts: EasyPrompt): GenerateContentRequest {
        return GenerateContentRequest(
            contents = prompts.map { prompt ->
                when (prompt) {
                    is EasyPrompt.TextPrompt -> Content(
                        role = prompt.role,
                        listOf(TextPart(prompt.text))
                    )
                }
            }
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