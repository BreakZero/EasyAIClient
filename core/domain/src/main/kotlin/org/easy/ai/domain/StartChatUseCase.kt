package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.ModelPlatform
import javax.inject.Inject

class StartChatUseCase @Inject internal constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val exactlyChatModelUseCase: GetExactlyChatModelUseCase
) {
    operator fun invoke(chatId: String?, modelPlatform: ModelPlatform? = null): Flow<Unit> {
        return userPreferencesDataSource.userData.map {
            val (model, apiKey) = getApiKeyByPriority(modelPlatform, it.apiKeys)
            if (apiKey.isNullOrBlank()) throw IllegalStateException("api key not set yet.")
            val chatRepository = exactlyChatModelUseCase(model)
            chatRepository.startChat(chatId)
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