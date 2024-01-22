package org.easy.ai.multimodal

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
internal data class PromptInputContent(
    val prompt: String = "",
    val images: List<ByteArray>? = null
)

internal sealed interface MultiModalEvent {}
