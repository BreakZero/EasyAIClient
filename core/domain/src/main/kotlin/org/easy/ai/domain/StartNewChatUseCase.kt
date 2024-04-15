package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.aimodel.ChatGPTModelRepository
import org.easy.ai.data.aimodel.GeminiModelRepository
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.ModelPlatform
import javax.inject.Inject

class StartNewChatUseCase @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val geminiModelRepository: GeminiModelRepository,
    private val chatGPTRepository: ChatGPTModelRepository
) {
    operator fun invoke(
        modelPlatform: ModelPlatform = ModelPlatform.GEMINI
    ): Flow<Chat> {
        return userPreferencesDataSource.userData.map { userData ->
            val (model, apiKey) = getApiKeyByPriority(modelPlatform, userData.apiKeys)
            if (apiKey.isNullOrBlank()) throw IllegalStateException("api key not set yet.")
            Chat(
                apiKey = apiKey,
                chatPlugin = when (model) {
                    ModelPlatform.GEMINI -> geminiModelRepository
                    ModelPlatform.CHAT_GPT -> chatGPTRepository
                }
            )
        }
    }

    private fun getApiKeyByPriority(
        modelPlatform: ModelPlatform? = null,
        apiKeys: Map<String, String>
    ): Pair<ModelPlatform, String?> {
        if (apiKeys.isEmpty()) throw IllegalStateException("Not api key setting up yet...")
        val result = when {
            modelPlatform != null -> Pair(modelPlatform, apiKeys[modelPlatform.name])
            apiKeys.containsKey(ModelPlatform.GEMINI.name) -> Pair(
                ModelPlatform.GEMINI,
                apiKeys[ModelPlatform.GEMINI.name]
            )

            apiKeys.containsKey(ModelPlatform.CHAT_GPT.name) -> Pair(
                ModelPlatform.CHAT_GPT,
                apiKeys[ModelPlatform.CHAT_GPT.name]
            )

            else -> throw IllegalStateException("No supported model: ${apiKeys.keys} ")
        }
        return result
    }
}