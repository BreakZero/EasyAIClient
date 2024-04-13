package org.easy.ai.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
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
import kotlinx.serialization.json.Json
import org.easy.ai.network.gemini.GeminiRestApi
import org.easy.ai.network.gemini.GeminiRestApiController
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiRestHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatGPTRestHttpClient

internal val JSON = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
}

private fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(JSON)
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }
        config()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @GeminiRestHttpClient
    @Provides
    fun providesGeminiRestHttpClient(): HttpClient {
        return httpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "generativelanguage.googleapis.com"
                    path("v1/]")
                }
                contentType(ContentType.Application.Json)
                header("x-goog-api-client", "easyai-android")
            }
        }
    }

    @ChatGPTRestHttpClient
    @Provides
    fun providesChatGPTRestHttpClient(): HttpClient {
        return httpClient {

        }
    }

    @Provides
    fun providesGeminiRestApi(
        @GeminiRestHttpClient httpClient: HttpClient
    ): GeminiRestApi {
        return GeminiRestApiController(httpClient)
    }
}