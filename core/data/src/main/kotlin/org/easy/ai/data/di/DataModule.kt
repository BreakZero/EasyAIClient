package org.easy.ai.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.data.repository.GeminiLocalChatRepository
import org.easy.ai.data.repository.chat.ChatRepository
import org.easy.ai.data.repository.model.ChatGPTRepository
import org.easy.ai.data.repository.model.GeminiChatRepository
import org.easy.ai.data.repository.model.ModelChatRepository
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
        geminiRepository: GeminiChatRepository
    ): ModelChatRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.CHAT_GPT)
    abstract fun bindChatGPTRepository(
        chatGPTRepository: ChatGPTRepository
    ): ModelChatRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindGeminiChatLocalRepository(
        geminiLocalChatRepository: GeminiLocalChatRepository
    ): LocalChatRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindGeminiChatRepository(
        geminiChatRepository: org.easy.ai.data.repository.chat.GeminiChatRepository
    ): ChatRepository
}