package org.easy.ai.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.LocalChatRepository
import org.easy.ai.data.repository.chat.AiModelChatRepository
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
}