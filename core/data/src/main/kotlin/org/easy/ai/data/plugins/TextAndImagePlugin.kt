package org.easy.ai.data.plugins

import kotlinx.coroutines.flow.Flow

interface TextAndImagePlugin {
    fun generateContentStream(apiKey: String, prompt: String, images: List<ByteArray>): Flow<String>
}