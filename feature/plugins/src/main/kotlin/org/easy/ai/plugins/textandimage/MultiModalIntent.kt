package org.easy.ai.plugins.textandimage

import androidx.compose.runtime.Stable

@Stable
internal data class MultiModalUiState(
    val images: List<ByteArray>? = null,
    val promptResult: String? = null,
    val error: String? = null,
    val inProgress: Boolean = false
)