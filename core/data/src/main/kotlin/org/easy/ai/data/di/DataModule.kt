package org.easy.ai.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.repository.model.ChatGPTRepository
import org.easy.ai.data.repository.model.GeminiRepository
import org.easy.ai.data.repository.model.ModelRepository
import org.easy.ai.model.ModelPlatform
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelPlatformQualifier(val model: ModelPlatform)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindGeminiRepository(
        geminiRepository: GeminiRepository
    ): ModelRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.CHAT_GPT)
    abstract fun bindChatGPTRepository(
        chatGPTRepository: ChatGPTRepository
    ): ModelRepository
}