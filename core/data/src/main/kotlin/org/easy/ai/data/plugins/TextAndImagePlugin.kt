package org.easy.ai.data.plugins

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface TextAndImagePlugin {
    fun generateContentStream(apiKey: String, prompt: String, images: List<Bitmap>?): Flow<String>
}