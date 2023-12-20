package org.easy.gemini.model

data class UserData(
    val modelName: String,
    val apiKey: String
) {
    fun validate(): UserDataValidateResult {
        return when {
            apiKey.isBlank() -> UserDataValidateResult.API_KEY_NULL
            modelName.isBlank() -> UserDataValidateResult.MODEL_NAME_NULL
            else -> UserDataValidateResult.NORMAL
        }
    }
}

enum class UserDataValidateResult {
    MODEL_NAME_NULL, API_KEY_NULL, NORMAL
}
