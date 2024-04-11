package org.easy.ai.model

sealed interface EasyPrompt {
    data class TextPrompt(val role: String, val text: String): EasyPrompt
}
