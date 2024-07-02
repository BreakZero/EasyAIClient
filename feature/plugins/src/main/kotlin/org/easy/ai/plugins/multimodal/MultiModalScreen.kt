package org.easy.ai.plugins.multimodal

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.common.tools.ImagePicker
import org.easy.ai.plugins.component.AiResultContentView
import org.easy.ai.plugins.component.InputView
import org.easy.ai.plugins.component.SentPromptView
import org.easy.ai.system.ui.localDim

@Composable
internal fun MultiModalRoute(popBack: () -> Unit) {
    val multiModalViewModel: MultiModalViewModel = hiltViewModel()
    val imagePicker = ImagePicker(LocalContext.current)
    imagePicker.RegisterPicker(multiModalViewModel::onImageChanged, maxItems = 4)
    val uiState by multiModalViewModel.modalUiState.collectAsStateWithLifecycle()
    val selectedImages by multiModalViewModel.selectedImages.collectAsStateWithLifecycle()

    MultiScreen(
        uiState = uiState,
        selectedImages = selectedImages,
        onImagePicked = imagePicker::startImagePicker,
        onSubmit = multiModalViewModel::submit,
        popBack = popBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MultiScreen(
    uiState: ModalUiState,
    selectedImages: List<ByteArray>?,
    onImagePicked: () -> Unit,
    onSubmit: (String) -> Unit,
    popBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("Gemini Text And Image Input") },
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            val imageBitmaps = rememberBitmapFromBytes(imageBytes = selectedImages)
            InputView(
                images = imageBitmaps,
                onImagePicked = onImagePicked,
                onSubmit = onSubmit
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = MaterialTheme.localDim.spaceMedium),
            contentPadding = PaddingValues(bottom = MaterialTheme.localDim.space12),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceMedium)
        ) {
            uiState.promptContent?.let {
                item {
                    SentPromptView(promptContent = it)
                }
            }
            uiState.result?.let {
                item {
                    AiResultContentView(textContent = it, inProgress = uiState.inProgress)
                }
            }
        }
    }
}


@Composable
internal fun rememberBitmapFromBytes(imageBytes: List<ByteArray>?): List<ImageBitmap>? {
    return remember(imageBytes) {
        imageBytes?.map {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
        }
    }
}
