package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import org.easy.ai.data.repository.UserPreferencesRepository
import javax.inject.Inject

class ApiKeyCheckUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.hasApiKey()
    }
}