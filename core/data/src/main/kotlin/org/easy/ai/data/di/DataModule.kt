package org.easy.ai.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.aimodel.ChatGPTModelRepository
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.data.repository.chat.AiModelChatRepository
import org.easy.ai.data.repository.chat.ChatGPTChatRepository
import org.easy.ai.data.repository.chat.GeminiChatRepository
import org.easy.ai.model.ModelPlatform
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelPlatformQualifier(val model: ModelPlatform)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindLocalChatRepository(
        localChatRepository: LocalChatRepository
    ): ChatRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindGeminiRepository(
        geminiChatRepository: GeminiChatRepository
    ): AiModelChatRepository

    @Binds
    @ModelPlatformQualifier(ModelPlatform.CHAT_GPT)
    abstract fun bindChatGPTRepository(
        chatGPTChatRepository: ChatGPTChatRepository
    ): AiModelChatRepository


    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindGeminiChatPlugin(
        geminiModel: GeminiModelRepository
    ): ChatPlugin

    @Binds
    @ModelPlatformQualifier(ModelPlatform.GEMINI)
    abstract fun bindChatGPTChatPlugin(
        chatGptModel: ChatGPTModelRepository
    ): ChatPlugin
}