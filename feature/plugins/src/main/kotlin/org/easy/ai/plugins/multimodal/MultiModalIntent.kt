package org.easy.ai.plugins.multimodal

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal data class ModalUiState(
    val promptContent: PromptContent? = null,
    val result: String? = null,
    val error: String? = null,
    val inProgress: Boolean = false
)


@Immutable
internal data class PromptContent(
    val prompt: String,
    val images: List<Bitmap>? = null
)