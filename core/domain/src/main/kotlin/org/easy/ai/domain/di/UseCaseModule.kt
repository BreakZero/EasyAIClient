package org.easy.ai.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.di.ModelPlatformQualifier
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.data.repository.chat.ChatRepository
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.domain.GeminiModelInitialUseCase
import org.easy.ai.domain.MessageSendingUseCase
import org.easy.ai.model.ModelPlatform
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun providesGeminiModelInitialUseCase(
        userPreferencesRepository: UserPreferencesRepository
    ): GeminiModelInitialUseCase {
        return GeminiModelInitialUseCase(userPreferencesRepository)
    }

//    @Provides
//    @Singleton
//    fun providesMessageSendingUseCase(
//        userPreferencesDataSource: UserPreferencesDataSource,
//        @ModelPlatformQualifier(ModelPlatform.GEMINI) geminiChatRepository: ChatRepository
//    ): MessageSendingUseCase {
//        return MessageSendingUseCase(userPreferencesDataSource, geminiChatRepository)
//    }
}