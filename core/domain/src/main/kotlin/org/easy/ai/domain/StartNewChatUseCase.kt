package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.aimodel.ChatGPTModelRepository
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.data.repository.UserDataRepository
import org.easy.ai.model.AiModel
import javax.inject.Inject

class StartNewChatUseCase @Inject internal constructor(
    private val offlineUserDataRepository: UserDataRepository,
    private val geminiModelRepository: GeminiModelRepository,
    private val chatGPTRepository: ChatGPTModelRepository
) {
    operator fun invoke(): Flow<Chat> {
        return offlineUserDataRepository.userData.map { userData ->
            val chatAiModel = userData.defaultChatModel ?: AiModel.GEMINI
            val apiKey = userData.apiKeys[chatAiModel.name]
                ?: throw IllegalStateException("${chatAiModel.name}'s api key not set yet.")

            Chat(
                apiKey = apiKey,
                chatPlugin = when (chatAiModel) {
                    AiModel.GEMINI -> geminiModelRepository
                    AiModel.CHAT_GPT -> chatGPTRepository
                }
            )
        }
    }
}