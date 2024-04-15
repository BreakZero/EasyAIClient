package org.easy.ai.plugins.multimodal

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.easy.ai.common.tools.ImagePicker
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme
import org.easy.ai.system.ui.localDim
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MultiModalRoute(
    popBack: () -> Unit
) {
    val multiModalViewModel: MultiModalViewModel = hiltViewModel()
    val imagePicker = ImagePicker(LocalContext.current)
    imagePicker.RegisterPicker(multiModalViewModel::onImageChanged)
    val uiState by multiModalViewModel.uiState.collectAsStateWithLifecycle()

    MultiModalScreen(
        prompt = multiModalViewModel.promptTextField,
        uiState = uiState,
        onImagePicked = imagePicker::startImagePicker,
        popBack = popBack,
        submit = multiModalViewModel::submitPrompt
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MultiModalScreen(
    prompt: TextFieldState = rememberTextFieldState(),
    uiState: MultiModalUiState,
    onImagePicked: () -> Unit,
    popBack: () -> Unit,
    submit: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Gemini Multi Modal") },
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.localDim.spaceMedium),
                onClick = submit,
                enabled = !uiState.inProgress
            ) {
                Text(text = if (uiState.inProgress) "Generating..." else "Submit Prompt")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.localDim.spaceMedium)
        ) {
            PromptEditor(prompt)
            Spacer(modifier = Modifier.height(MaterialTheme.localDim.space24))
            val images = rememberBitmapFromBytes(imageBytes = uiState.images)
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
                            .clickable(onClick = onImagePicked)
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

            HorizontalDivider(
                modifier = Modifier.padding(vertical = MaterialTheme.localDim.spaceSmall)
            )

            uiState.error?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }
            uiState.generateResult?.let {
                Text(style = MaterialTheme.typography.titleMedium, text = it)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PromptEditor(
    prompt: TextFieldState
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(8.dp))
            .height(160.dp)
            .padding(8.dp)
    ) {
        BasicTextField2(
            state = prompt,
            lineLimits = TextFieldLineLimits.MultiLine(3, 8),
            scrollState = scrollState,
            textStyle = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1.0f),
        )
        val direction = LayoutDirection.Rtl
        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            Slider(modifier = Modifier
                .graphicsLayer {
                    rotationZ = 270f
                    transformOrigin = TransformOrigin(0f, 0f)
                }

                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }
                .weight(.2f),
                value = scrollState.value.toFloat(), onValueChange = {
                    coroutineScope.launch { scrollState.scrollTo(it.roundToInt()) }
                }, valueRange = 0f..scrollState.maxValue.toFloat()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
private fun PromptEditor_Preview() {
    EasyAITheme(
    ) {
        PromptEditor(rememberTextFieldState())
    }
}