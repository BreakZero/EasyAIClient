package org.easy.ai.multimodal

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.common.tools.ImagePicker
import org.easy.ai.system.ui.localDim

@Composable
fun MultiModalRoute(
    popBack: () -> Unit
) {
    val multiModalViewModel: MultiModalViewModel = hiltViewModel()
    val promptInputContentUiState by multiModalViewModel.inputContentUiState.collectAsStateWithLifecycle()

    val imagePicker = ImagePicker(LocalContext.current)
    imagePicker.registerPicker(multiModalViewModel::onImagesChanged)

    MultiModalScreen(
        promptInputContentUiState,
        multiModalViewModel::onPromptChanged,
        imagePicker::startImagePicker,
        popBack,
        multiModalViewModel::sendPrompt
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MultiModalScreen(
    inputContent: PromptInputContent,
    onPromptChanged: (String) -> Unit,
    onImagePicked: () -> Unit,
    popBack: () -> Unit,
    commit: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                })
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.localDim.spaceMedium),
                onClick = commit
            ) {
                Text(text = "Send Prompt")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.localDim.spaceMedium)
        ) {
            TextField(
                value = inputContent.prompt,
                onValueChange = onPromptChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = MaterialTheme.localDim.spaceXXLarge)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.localDim.space24))
            val images = rememberBitmapFromBytes(imageBytes = inputContent.images)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceSmall)
            ) {
                images?.let { imageBitmaps ->
                    items(imageBitmaps) { imageBitmap ->
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.0f)
                                .clip(RoundedCornerShape(MaterialTheme.localDim.spaceExtraSmall)),
                            bitmap = imageBitmap,
                            contentDescription = null
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                            .clip(RoundedCornerShape(MaterialTheme.localDim.spaceExtraSmall))
                            .clickable {
                                onImagePicked()
                            }
                            .padding(MaterialTheme.localDim.space24)
                            .border(
                                1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(imageVector = Icons.Default.Add, contentDescription = null)
                    }
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
