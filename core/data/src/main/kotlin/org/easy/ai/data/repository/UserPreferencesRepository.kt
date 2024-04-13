package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.ModelPlatform
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userDataSource: UserPreferencesDataSource
)  {
    val userData = userDataSource.userData

    fun hasApiKey(): Flow<Boolean> {
        return userData.map {
            it.apiKeys.isNotEmpty()
        }
    }

    suspend fun addApiKey(modelPlatform: ModelPlatform, apiKey: String) {
        userDataSource.addApiKey(modelPlatform, apiKey)
    }

    suspend fun updateAiModel(modelName: String) = userDataSource.updateAiModel(modelName)

    suspend fun updateApiKey(apiKey: String) = userDataSource.updateApiKey(apiKey)

    suspend fun updateAutomaticSaveChat(isAutomaticSave: Boolean) = userDataSource.updateAutomaticSaveChat(isAutomaticSave)
}