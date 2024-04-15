package org.easy.ai.multimodal

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.easy.ai.common.BaseViewModel
import org.easy.ai.domain.GeminiModelInitialUseCase
import javax.inject.Inject

@HiltViewModel
internal class MultiModalViewModel @Inject constructor(
    modelInitialUseCase: GeminiModelInitialUseCase
) : BaseViewModel<MultiModalEvent>() {
    private var geminiModel: GenerativeModel? = null

    companion object {
        private const val CONTENT_LIMIT_SIZE = 4 * 1024 * 1024
    }

    private val _promptInputContent = MutableStateFlow(PromptInputContent())

    val inputContentUiState = _promptInputContent.asStateFlow()

    init {
        modelInitialUseCase("gemini-pro-vision").onEach {
            geminiModel = it
        }.launchIn(viewModelScope)
    }

    fun onPromptChanged(prompt: String) {
        _promptInputContent.update {
            it.copy(prompt = prompt, errorMessage = null)
        }
    }

    fun onImagesChanged(bytes: List<ByteArray>) {
        _promptInputContent.update {
            it.copy(images = bytes, errorMessage = null)
        }
    }

    private fun checkPrompt(inputContent: PromptInputContent): String? {
        var error: String? = null
        if (inputContent.prompt.isBlank()) {
            error = "prompt can not be empty..."
        } else if (inputContent.images.isNullOrEmpty()) {
            error = "images can not be empty..."
        } else {
            val totalSize = inputContent.let {
                it.prompt.encodeToByteArray().size + it.images!!.sumOf { image -> image.size }
            }
            if (totalSize > CONTENT_LIMIT_SIZE) {
                error = "the entire prompt is too large, 4MB limited"
            }
        }
        return error
    }

    fun sendPrompt() {
        val promptInputContent = _promptInputContent.value

        val error = checkPrompt(promptInputContent)

        if (error != null) {
            _promptInputContent.update {
                it.copy(errorMessage = error)
            }
            return
        }
        _promptInputContent.update { it.copy(result = "", errorMessage = null) }
        viewModelScope.launch {
            val bitmaps = promptInputContent.images?.map {
                BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
                    .asAndroidBitmap()
            }
            val content = content {
                bitmaps?.map {
                    image(it)
                }
                text(promptInputContent.prompt)
            }
            try {
                val result = geminiModel?.generateContent(content)
                _promptInputContent.update { it.copy(result = result?.text) }
            } catch (e: Exception) {
                e.printStackTrace()
                _promptInputContent.update { it.copy(result = "", errorMessage = e.message) }
            }

            bitmaps?.forEach { it.recycle() }
        }
    }
}