package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.datastore.UserPreferencesDataSource
import org.easy.ai.model.AiModel
import org.easy.ai.model.UserData
import javax.inject.Inject

class OfflineUserDataRepository @Inject constructor(
    private val userDataSource: UserPreferencesDataSource
) : UserDataRepository{
    override val userData: Flow<UserData>
        get() = userDataSource.userData

    override suspend fun setAiModelApiKey(model: AiModel, apiKey: String) {
        userDataSource.addApiKey(model, apiKey)
    }

    override suspend fun setDefaultChatAiModel(model: AiModel) {
        userDataSource.updateChatAiModel(model)
    }
}