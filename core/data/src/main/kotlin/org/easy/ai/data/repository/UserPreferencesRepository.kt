package org.easy.ai.data.repository

import org.easy.ai.datastore.UserPreferencesDataSource
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userDataSource: UserPreferencesDataSource
) {
    val userData = userDataSource.userData

    suspend fun setModelName(modelName: String) {
        userDataSource.setModelName(modelName)
    }

    suspend fun setApiKey(apiKey: String) {
        userDataSource.setApiKey(apiKey)
    }
}