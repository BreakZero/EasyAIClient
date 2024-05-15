package org.easy.ai.plugins.component

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.DecorationBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme
import org.easy.ai.system.ui.localDim

internal enum class DisplayLevel {
    MINIMAL, MEDIUM, FULLSCREEN
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun InputView(
    modifier: Modifier = Modifier,
    images: List<ImageBitmap>? = null,
    onImagePicked: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var displayLevel by remember { mutableStateOf(DisplayLevel.MINIMAL) }
    var isPromptEmpty by remember {
        mutableStateOf(true)
    }
    val transition = updateTransition(displayLevel, label = "full screen state")

    val radius by transition.animateDp(
        label = "radius",
        transitionSpec = { tween(durationMillis = 600) }) {
        if (it == DisplayLevel.MEDIUM) 20.dp else 0.dp
    }

    var hasFocus by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = images, key2 = hasFocus) {
        displayLevel = if (images.isNullOrEmpty()) {
            if (hasFocus) {
                DisplayLevel.MEDIUM
            } else {
                DisplayLevel.MINIMAL
            }
        } else {
            DisplayLevel.FULLSCREEN
        }
    }

    val height by transition.animateDp(
        label = "height",
        transitionSpec = { tween(durationMillis = 600) }
    ) {
        when (it) {
            DisplayLevel.MINIMAL -> 56.dp
            DisplayLevel.MEDIUM -> screenWidth.times(9).div(16)
            DisplayLevel.FULLSCREEN -> screenHeight
        }
    }

    val coroutineScope = rememberCoroutineScope()

    fun toggleFullScreen() {
        coroutineScope.launch {
            when (displayLevel) {
                DisplayLevel.MINIMAL -> Unit
                DisplayLevel.MEDIUM -> {
                    displayLevel = DisplayLevel.FULLSCREEN
                }

                DisplayLevel.FULLSCREEN -> {
                    displayLevel = DisplayLevel.MEDIUM
                }
            }
        }
    }

    val enterContent = rememberTextFieldState()

    LaunchedEffect(Unit) {
        enterContent.textAsFlow().debounce(300)
            .distinctUntilChanged()
            .collect {
                isPromptEmpty = it.isBlank()
            }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                this.shadowElevation = radius.value
                this.clip = true
                this.shape = RoundedCornerShape(
                    topEnd = radius,
                    topStart = radius,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            }
            .background(MaterialTheme.colorScheme.background)
            .let {
                if (displayLevel == DisplayLevel.MINIMAL) it
                    .padding(horizontal = MaterialTheme.localDim.spaceMedium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(56.dp)
                    ) else it
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (displayLevel == DisplayLevel.FULLSCREEN) {
                images?.let {
                    ImageGridView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0F)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        images = it
                    )
                }
            }
            BasicTextField2(
                state = enterContent,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .onFocusChanged {
                        hasFocus = it.hasFocus
                    },
                decorator = @Composable {
                    val interactionSource = remember { MutableInteractionSource() }
                    DecorationBox(
                        value = enterContent.text.toString(),
                        innerTextField = it,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        container = {},
                        placeholder = {
                            Text(text = "Type, talk, or share a photo")
                        }
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        when (displayLevel) {
                            DisplayLevel.MINIMAL -> placeable.place(0, 0)

                            DisplayLevel.MEDIUM, DisplayLevel.FULLSCREEN -> placeable.place(
                                0 - (screenWidth.roundToPx() - placeable.width) / 2,
                                0
                            )
                        }
                    }
                }
                .padding(4.dp)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(100.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { focusManager.clearFocus() }) {
                Icon(
                    imageVector = Icons.Default.Mic, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            IconButton(onClick = onImagePicked) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        if (displayLevel != DisplayLevel.MINIMAL) {
            IconButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                enabled = !isPromptEmpty,
                onClick = {
                    onSubmit(enterContent.text.toString())
                    focusManager.clearFocus()
                    enterContent.clearText()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }

            Icon(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3F))
                    .clickable { toggleFullScreen() }
                    .padding(4.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                imageVector = if (displayLevel == DisplayLevel.FULLSCREEN) Icons.Default.CloseFullscreen else Icons.Default.OpenInFull,
                contentDescription = null
            )
        }
    }
}

@ThemePreviews
@Composable
private fun InputView_Preview() {
    EasyAITheme {
        InputView(modifier = Modifier, null, {}, {})
    }
}