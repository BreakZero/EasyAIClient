package org.easy.ai.domain

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.data.repository.UserDataRepository
import org.easy.ai.model.AiModel
import javax.inject.Inject

class TextAndImageGeneratingUseCase @Inject internal constructor(
    private val offlineUserDataRepository: UserDataRepository,
    private val geminiModelRepository: GeminiModelRepository
) {
    operator fun invoke(prompt: String, images: List<ByteArray>?): Flow<String> {
        return channelFlow {
            launch(CoroutineName("GenerateForTextImageInput")) {
                val userData = offlineUserDataRepository.userData.first()
                val apiKey = userData.apiKeys[AiModel.GEMINI.name] ?: throw IllegalStateException(
                    "${AiModel.GEMINI.name}'s api key not set yet."
                )
                geminiModelRepository.generateContentStream(apiKey, prompt, images).collect {
                    trySend(it)
                }
            }
        }
    }
}