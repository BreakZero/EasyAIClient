package org.easy.ai.model

data class UserData(
    val apiKeys: Map<String, String>,
    val activatedModel: AiModel = AiModel.GEMINI,
    val isAutomaticSaveChat: Boolean
) {
    fun validate(): UserDataValidateResult {
        return when {
            apiKeys.isEmpty() -> UserDataValidateResult.API_KEY_NULL
            else -> UserDataValidateResult.NORMAL
        }
    }
}

enum class UserDataValidateResult {
    MODEL_NAME_NULL, API_KEY_NULL, NORMAL
}
