package org.easy.ai.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.domain.GeminiModelInitialUseCase
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
}