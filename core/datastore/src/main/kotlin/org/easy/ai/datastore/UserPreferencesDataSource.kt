package org.easy.ai.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.easy.ai.model.AiModel
import org.easy.ai.model.UserData
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            activatedModel = when (it.activatedModel) {
                AiModelProto.AI_CHAT_GPT -> AiModel.CHAT_GPT
                else -> AiModel.GEMINI
            },
            apiKeys = it.apiKeysMap,
            isAutomaticSaveChat = it.automaticSaveChat
        )
    }

    suspend fun addApiKey(aiModel: AiModel, apiKey: String) {
        userPreferences.updateData {
            it.copy {
                apiKeys.put(aiModel.name, apiKey)
            }
        }
    }

    suspend fun updateAiModel(aiModel: AiModel) {
        userPreferences.updateData {
            it.copy {
                activatedModel = when (aiModel) {
                    AiModel.GEMINI -> AiModelProto.AI_GEMINI
                    AiModel.CHAT_GPT -> AiModelProto.AI_CHAT_GPT
                }
            }
        }
    }
}