package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.aimodel.ChatGPTModelRepository
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.AiModel
import javax.inject.Inject

class StartNewChatUseCase @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val geminiModelRepository: GeminiModelRepository,
    private val chatGPTRepository: ChatGPTModelRepository
) {
    operator fun invoke(
        aiModel: AiModel = AiModel.GEMINI
    ): Flow<Chat> {
        return userPreferencesDataSource.userData.map { userData ->
            val (model, apiKey) = getApiKeyByPriority(aiModel, userData.apiKeys)
            if (apiKey.isNullOrBlank()) throw IllegalStateException("api key not set yet.")
            Chat(
                apiKey = apiKey,
                chatPlugin = when (model) {
                    AiModel.GEMINI -> geminiModelRepository
                    AiModel.CHAT_GPT -> chatGPTRepository
                }
            )
        }
    }

    private fun getApiKeyByPriority(
        aiModel: AiModel? = null,
        apiKeys: Map<String, String>
    ): Pair<AiModel, String?> {
        if (apiKeys.isEmpty()) throw IllegalStateException("Not api key setting up yet...")
        val result = when {
            aiModel != null -> Pair(aiModel, apiKeys[aiModel.name])
            apiKeys.containsKey(AiModel.GEMINI.name) -> Pair(
                AiModel.GEMINI,
                apiKeys[AiModel.GEMINI.name]
            )

            apiKeys.containsKey(AiModel.CHAT_GPT.name) -> Pair(
                AiModel.CHAT_GPT,
                apiKeys[AiModel.CHAT_GPT.name]
            )

            else -> throw IllegalStateException("No supported model: ${apiKeys.keys} ")
        }
        return result
    }
}