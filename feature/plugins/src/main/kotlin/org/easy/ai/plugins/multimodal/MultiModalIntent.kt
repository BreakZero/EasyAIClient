package org.easy.ai.plugins.multimodal

import androidx.compose.runtime.Stable

@Stable
internal data class MultiModalUiState(
    val images: List<ByteArray> = emptyList(),
    val generateResult: String? = null
)