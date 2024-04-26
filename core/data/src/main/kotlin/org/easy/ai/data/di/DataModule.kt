package org.easy.ai.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import org.easy.ai.data.aimodel.ChatGPTModelRepository
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.data.plugins.ChatPlugin
import org.easy.ai.data.plugins.MultiModalPlugin
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.OfflineChatRepository
import org.easy.ai.data.repository.OfflineUserDataRepository
import org.easy.ai.data.repository.UserDataRepository
import org.easy.ai.model.AiModel

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelPlatformQualifier(val model: AiModel)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindOfflineChatRepository(
        offlineChatRepository: OfflineChatRepository
    ): ChatRepository

    @Binds
    abstract fun bindOfflineUserDataRepository(
        offlineUserDataRepository: OfflineUserDataRepository
    ): UserDataRepository

    @Binds
    @ModelPlatformQualifier(AiModel.GEMINI)
    abstract fun bindGeminiChatPlugin(geminiModel: GeminiModelRepository): ChatPlugin

    @Binds
    @ModelPlatformQualifier(AiModel.GEMINI)
    abstract fun bindChatGPTChatPlugin(chatGptModel: ChatGPTModelRepository): ChatPlugin

    @Binds
    @ModelPlatformQualifier(AiModel.GEMINI)
    abstract fun bindGeminiMultiModalPlugin(geminiModel: GeminiModelRepository): MultiModalPlugin
}