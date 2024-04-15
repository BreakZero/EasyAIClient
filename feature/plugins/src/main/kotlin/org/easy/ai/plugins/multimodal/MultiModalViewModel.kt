package org.easy.ai.plugins.multimodal

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
internal class MultiModalViewModel @Inject constructor(): ViewModel() {
    companion object {
        private const val CONTENT_LIMIT_SIZE = 4 * 1024 * 1024
    }

    val promptTextField = TextFieldState("")
    private val _uiState = MutableStateFlow(MultiModalUiState())

    val uiState = _uiState.asStateFlow()

    fun onImageChanged(imagesByte: List<ByteArray>) {
        _uiState.update {
            it.copy(images = imagesByte)
        }
    }

    private fun validatePrompt(): String? {
        if (promptTextField.text.toString().isBlank()) {
            return "prompt can not be empty..."
        }
        with(_uiState.value) {
            if (images.isEmpty()) {
                return "images can not be empty..."
            }
            val promptSize = promptTextField.text.toString().encodeToByteArray().size
            val totalSize = images.sumOf { image -> image.size } + promptSize
            if (totalSize > CONTENT_LIMIT_SIZE) {
                return "the entire prompt is too large, 4MB limited"
            }
        }
        return null
    }

    fun submitPrompt() {
        val error = validatePrompt()
        if (error != null) {
            // error handling
            return
        }
        val images = _uiState.value.images
        viewModelScope.launch {
            val bitmaps = images.map {
                BitmapFactory.decodeByteArray(it,0,it.size).asImageBitmap().asAndroidBitmap()
            }

            // generate request

            bitmaps.forEach { it.recycle() }
        }
    }
}