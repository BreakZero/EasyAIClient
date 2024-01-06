package org.easy.ai.data.repository

import org.easy.ai.datastore.UserPreferencesDataSource
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userDataSource: UserPreferencesDataSource
)  {
    val userData = userDataSource.userData

    suspend fun updateAiModel(modelName: String) = userDataSource.updateAiModel(modelName)

    suspend fun updateApiKey(apiKey: String) = userDataSource.updateApiKey(apiKey)

    suspend fun updateAutomaticSaveChat(isAutomaticSave: Boolean) = userDataSource.updateAutomaticSaveChat(isAutomaticSave)
}