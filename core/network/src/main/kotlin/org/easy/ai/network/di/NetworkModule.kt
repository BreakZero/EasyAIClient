package org.easy.ai.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.easy.ai.network.ChatGPTApi
import retrofit2.Retrofit
import javax.inject.Singleton

object NetworkHost {
    internal const val CHAT_GPT_BASE_URL = ""
}

@OptIn(ExperimentalSerializationApi::class)
private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = true
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            var request: Request? = null
            val original = chain.request()
            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", "Bearer ")
            request = requestBuilder.build()
            chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun provideChatGPTApi(
        okHttpClient: OkHttpClient
    ): ChatGPTApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(NetworkHost.CHAT_GPT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ChatGPTApi::class.java)
    }
}