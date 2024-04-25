package org.easy.ai.data.repository

import kotlinx.coroutines.flow.Flow
import org.easy.ai.model.AiModel
import org.easy.ai.model.UserData

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setAiModelApiKey(model: AiModel, apiKey: String)

    suspend fun selectedAiModel(model: AiModel)
}
