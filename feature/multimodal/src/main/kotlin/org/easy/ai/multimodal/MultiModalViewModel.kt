package org.easy.ai.multimodal

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.easy.ai.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class MultiModalViewModel @Inject constructor() : BaseViewModel<MultiModalEvent>() {

    private val _promptInputContent = MutableStateFlow(PromptInputContent())

    val inputContentUiState = _promptInputContent.asStateFlow()

    fun onPromptChanged(prompt: String) {
        _promptInputContent.update {
            it.copy(prompt = prompt)
        }
    }

    fun onImagesChanged(bytes: List<ByteArray>) {
        _promptInputContent.update {
            it.copy(images = bytes)
        }
    }
}