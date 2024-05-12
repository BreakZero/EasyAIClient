package org.easy.ai.network

import android.accounts.NetworkErrorException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.easy.ai.network.di.JSON
import org.easy.ai.network.error.InvalidAPIKeyException
import org.easy.ai.network.gemini.GeminiRestApiController
import org.easy.ai.network.model.content
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GeminiApiTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())
    lateinit var apiController: GeminiRestApiController

    @Before
    fun setup() {
        val mockEngine =
            MockEngine { request ->
                println("=== ${request.headers["x-goog-api-key"]}")
                val type = request.headers["x-goog-api-key"]
                when (type) {
                    "error" -> {
                        respond(
                            content = ByteReadChannel(mockErrorResponse),
                            status = HttpStatusCode.BadRequest,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    else -> {
                        respond(
                            content = ByteReadChannel(geminiGenerateContentNormal),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }
                }
            }
        val httpClient =
            HttpClient(mockEngine) {
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
        val response = apiController.generateContent("", "", content { })
        Assert.assertEquals(
            "mock success response.",
            response.text
        )
    }

    @Test
    fun test_vision_success_response() = testScope.runTest {
        val response = apiController.generateContent("", "", content { })
        Assert.assertEquals(
            "mock success response.",
            response.text
        )
    }

    @Test(expected = InvalidAPIKeyException::class)
    fun test_null_response() = testScope.runTest {
        apiController.generateContent("error", "", content { })
    }
}