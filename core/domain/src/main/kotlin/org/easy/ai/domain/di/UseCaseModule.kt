package org.easy.ai.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

//    @Provides
//    @Singleton
//    fun providesMessageSendingUseCase(
//        userPreferencesDataSource: UserPreferencesDataSource,
//        @ModelPlatformQualifier(ModelPlatform.GEMINI) geminiChatRepository: ChatRepository
//    ): MessageSendingUseCase {
//        return MessageSendingUseCase(userPreferencesDataSource, geminiChatRepository)
//    }
}