package org.easy.ai.multimodal

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
internal data class PromptInputContent(
    val prompt: String = "",
    val images: List<ByteArray>? = null,
    val errorMessage: String? = null,
    val result: String? = null
)

internal sealed interface MultiModalEvent {}
