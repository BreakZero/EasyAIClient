package org.easy.ai.domain

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.repository.UserPreferencesRepository
import org.easy.ai.model.AIModel
import org.easy.ai.model.UserDataValidateResult
import javax.inject.Inject

class GeminiModelInitialUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(modelName: String = ""): Flow<GenerativeModel?> {
        return userPreferencesRepository.userData.map {
            val isValid = it.validate() == UserDataValidateResult.NORMAL
            val generativeModel = if (isValid) {
                GenerativeModel(modelName = it.modelName, apiKey = it.apiKey)
            } else null
            generativeModel
        }
    }
}