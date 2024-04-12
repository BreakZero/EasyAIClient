package org.easy.ai.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.data.di.ModelPlatformQualifier
import org.easy.ai.data.repository.ChatRepository
import org.easy.ai.data.repository.chat.AiModelChatRepository
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.EasyPrompt
import org.easy.ai.model.ModelPlatform
import javax.inject.Inject

class MessageSendingUseCase @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val localChatRepository: ChatRepository,
    @ModelPlatformQualifier(ModelPlatform.GEMINI) private val geminiChatRepository: AiModelChatRepository
) {
    operator fun invoke(prompt: EasyPrompt, modelPlatform: ModelPlatform? = null): Flow<String> {
        return userPreferencesDataSource.userData.map {
            val (model, apiKey) = getApiKeyByPriority(modelPlatform, it.apiKeys)
            if (apiKey.isNullOrBlank()) throw IllegalStateException("api key not set yet.")
            val chatRepository = getChatRepositoryByModelPlatform(model)
            chatRepository.sendMessage(apiKey, prompt)
        }
    }

    private fun genChatName(message: String, defaultLength: Int = 12): String {
        return if (message.length > defaultLength) {
            message.take(defaultLength) + "..."
        } else {
            message
        }
    }

    private fun getApiKeyByPriority(modelPlatform: ModelPlatform? = null, apiKeys: Map<String, String>): Pair<ModelPlatform, String?> {
        return if (modelPlatform != null) {
            Pair(modelPlatform, apiKeys[modelPlatform.name])
        } else {
            when {
                apiKeys.containsKey(ModelPlatform.GEMINI.name) -> Pair(ModelPlatform.GEMINI, apiKeys[ModelPlatform.GEMINI.name])
                apiKeys.containsKey(ModelPlatform.CHAT_GPT.name) -> Pair(ModelPlatform.CHAT_GPT, apiKeys[ModelPlatform.CHAT_GPT.name])
                else -> throw IllegalStateException("Not api key setting up yet...")
            }
        }
    }

    private fun getChatRepositoryByModelPlatform(modelPlatform: ModelPlatform): AiModelChatRepository {
        return when(modelPlatform) {
            ModelPlatform.GEMINI -> geminiChatRepository
            ModelPlatform.CHAT_GPT -> TODO()
        }
    }
}