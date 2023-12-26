package org.easy.ai.model

sealed interface AIModel {
    data class GPT35Turbo(val model: String = "", val maxToken: Int = 4096): AIModel
}