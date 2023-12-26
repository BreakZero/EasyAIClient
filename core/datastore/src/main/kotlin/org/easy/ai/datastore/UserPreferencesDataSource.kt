package org.easy.ai.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.easy.ai.model.UserData
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map {
        UserData(
            modelName = it.modelName,
            apiKey = it.apiKey
        )
    }

    suspend fun setModelName(modelName: String) {
        userPreferences.updateData {
            it.copy { this.modelName = modelName }
        }
    }

    suspend fun setApiKey(apiKey: String) {
        userPreferences.updateData {
            it.copy { this.apiKey = apiKey }
        }
    }
}