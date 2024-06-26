package org.easy.ai.common.tools

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ImagePicker @Inject constructor(
    @ActivityContext private val activityContext: Context
) {
    private lateinit var contentLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    @Composable
    fun RegisterPicker(onContentPicker: (List<ByteArray>) -> Unit, maxItems: Int = 9) {
        this.contentLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems),
            onResult = { uris ->
                val imagesByte = uris.mapNotNull { uri ->
                    activityContext.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                }
                onContentPicker(imagesByte)
            }
        )
    }



    fun startImagePicker() {
        contentLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}