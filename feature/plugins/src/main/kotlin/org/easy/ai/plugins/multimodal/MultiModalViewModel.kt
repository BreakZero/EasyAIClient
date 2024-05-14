package org.easy.ai.plugins.multimodal

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.easy.ai.domain.TextAndImageGeneratingUseCase
import javax.inject.Inject

@HiltViewModel
internal class MultiModalViewModel @Inject constructor(
    private val multiModalGeneratingUseCase: TextAndImageGeneratingUseCase
) : ViewModel() {
    companion object {
        private const val CONTENT_LIMIT_SIZE = 4 * 1024 * 1024
    }

    private val _selectedImages: MutableStateFlow<List<ByteArray>?> = MutableStateFlow(null)
    val selectedImages = _selectedImages.asStateFlow()

    private val _uiState: MutableStateFlow<ModalUiState> = MutableStateFlow(ModalUiState())
    val modalUiState = _uiState.asStateFlow()


    fun onImageChanged(imagesByte: List<ByteArray>) {
        _selectedImages.update { imagesByte }
    }

    private fun validatePrompt(prompt: String): String? {
        if (prompt.isBlank()) {
            return "prompt can not be empty..."
        }
        return _selectedImages.value?.let {
            val promptSize = prompt.encodeToByteArray().size
            val totalSize = it.sumOf { image -> image.size } + promptSize
            if (totalSize > CONTENT_LIMIT_SIZE) {
                "the entire prompt is too large, 4MB limited"
            } else null
        }
    }

    fun submit(prompt: String) {
        val error = validatePrompt(prompt)
        if (error != null) {
            // error handling
            _uiState.update { it.copy(result = error) }
            return
        }
        val bitmaps = _selectedImages.value?.map {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                .asAndroidBitmap()
        }
        val promptContent = PromptContent(prompt = prompt, images = bitmaps)

        _selectedImages.update { null }

        _uiState.update { it.copy(inProgress = true, result = null, promptContent = promptContent) }

        val generatedResult = StringBuilder()

        multiModalGeneratingUseCase(prompt, bitmaps)
            .onEach { result ->
                generatedResult.append(result)
                _uiState.update { it.copy(result = generatedResult.toString()) }
            }.catch { cause ->
                _uiState.update {
                    it.copy(
                        inProgress = false,
                        result = cause.message ?: "unknown generating error..."
                    )
                }
            }.onCompletion {
                _uiState.update {
                    it.copy(inProgress = false)
                }
            }.launchIn(viewModelScope)
    }
}