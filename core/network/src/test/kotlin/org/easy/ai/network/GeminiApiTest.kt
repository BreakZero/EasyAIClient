package org.easy.ai.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.easy.ai.network.di.JSON
import org.easy.ai.network.error.InvalidAPIKeyException
import org.easy.ai.network.error.UnsupportedUserLocationException
import org.easy.ai.network.gemini.GeminiRestApiController
import org.easy.ai.network.gemini.internal.GenerateContentRequest
import org.easy.ai.network.model.content
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GeminiApiTest {
    private val model = "gemini-pro"
    private val apiKey = "mock_api_key"

    private val testScope = TestScope(UnconfinedTestDispatcher())
    private lateinit var apiController: GeminiRestApiController

    private var type: ExpectedType = ExpectedType.UNDEFINED

    @Before
    fun setup() {
        val mockEngine = MockEngine { request ->
            val requestBody = request.body.toByteArray().decodeToString()
            val content = JSON.decodeFromString(GenerateContentRequest.serializer(), requestBody)

            val isStream = request.url.parameters.contains("alt")
            val apiKey_ = request.headers["x-goog-api-key"]

            if (apiKey_ != apiKey) {
                buildErrorResponse(text = INVALID_API_KEY_TEXT)
            } else {
                when (type) {
                    ExpectedType.UNDEFINED -> {
                        if (isStream) buildStreamResponse(text1 = SUCCESS_TEXT, text2 = "null")
                        else buildSuccessResponse(SUCCESS_TEXT)
                    }
                    ExpectedType.UNSUPPORTED_LOCATION -> buildErrorResponse(text = LOCATION_NOT_SUPPORTED)
                    ExpectedType.EMPTY -> TODO()
                }
            }
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(JSON)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "test_host"
                    path("v1/")
                }
                contentType(ContentType.Application.Json)
                header("x-goog-api-client", "easyai-android")
            }
        }
        apiController = GeminiRestApiController(httpClient)
    }

    @Test
    fun test_success_response() = testScope.runTest {
        type = ExpectedType.UNDEFINED
        val response = apiController.generateContent(
            apiKey = apiKey,
            model = model,
            content {
                role = "model"
                text("")
            }
        )
        Assert.assertEquals(
            SUCCESS_TEXT,
            response.text
        )
    }

    @Test
    fun test_success_response_stream() = testScope.runTest {
        type = ExpectedType.UNDEFINED
        val responseStream = apiController.generateContentStream(
            apiKey = apiKey,
            model = model,
            content { }
        )
        Assert.assertEquals(
            SUCCESS_TEXT,
            responseStream.first().text
        )
    }

    @Test(expected = InvalidAPIKeyException::class)
    fun test_invalid_response() = testScope.runTest {
        type = ExpectedType.UNDEFINED
        apiController.generateContent(
            apiKey = "",
            model = model,
            content { }
        )
    }

    @Test(expected = InvalidAPIKeyException::class)
    fun test_invalid_response_in_stream() = testScope.runTest {
        type = ExpectedType.UNDEFINED
        apiController.generateContentStream(
            apiKey = "",
            model = model,
            content { }
        ).collect()
    }

    @Test(expected = UnsupportedUserLocationException::class)
    fun test_location_not_supported() = testScope.runTest {
        type = ExpectedType.UNSUPPORTED_LOCATION
        apiController.generateContent(apiKey, model, content {
            role = "no-supported-location"
        })
    }
}