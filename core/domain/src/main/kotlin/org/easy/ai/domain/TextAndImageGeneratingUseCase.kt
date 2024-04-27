package org.easy.ai.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.AiModel

class TextAndImageGeneratingUseCase @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val geminiModelRepository: GeminiModelRepository
) {
    operator fun invoke(prompt: String, images: List<ByteArray>?): Flow<String> {
        return userPreferencesDataSource.userData.flatMapConcat { userData ->
            val apiKey = userData.apiKeys[AiModel.GEMINI.name] ?: throw IllegalStateException(
                "${AiModel.GEMINI.name}'s api key not set yet."
            )
            geminiModelRepository.generateContentStream(apiKey, prompt, images)
        }
    }
}