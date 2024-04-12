package org.easy.ai.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.easy.ai.model.ModelPlatform
import org.easy.ai.model.UserData
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            modelName = it.modelName,
            apiKeys = it.apiKeysMap,
            isAutomaticSaveChat = it.automaticSaveChat
        )
    }

    suspend fun updateAiModel(modelName: String) {
        userPreferences.updateData {
            it.copy { this.modelName = modelName }
        }
    }

    suspend fun updateApiKey(apiKey: String) {
        userPreferences.updateData {
            it.copy {
                apiKeys.put(ModelPlatform.GEMINI.name, apiKey)
            }
        }
    }

    suspend fun updateAutomaticSaveChat(isAutomaticSave: Boolean) {
        userPreferences.updateData {
            it.copy { this.automaticSaveChat = isAutomaticSave }
        }
    }
}