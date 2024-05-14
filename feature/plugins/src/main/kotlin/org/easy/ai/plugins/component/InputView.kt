package org.easy.ai.plugins.component

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme

private val DefaultRadius = 20.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun InputView(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val enterContent = rememberTextFieldState()
    var isFullScreen by remember { mutableStateOf(false) }
    val transition = updateTransition(isFullScreen, label = "full screen state")

    val radius by transition.animateDp(
        label = "radius",
        transitionSpec = { tween(durationMillis = 600) }) {
        if (it) 0.dp else DefaultRadius
    }

    val height by transition.animateDp(
        label = "height",
        transitionSpec = { tween(durationMillis = 600) }) {
        if (it) screenHeight else screenWidth.times(9).div(16)
    }

    val coroutineScope = rememberCoroutineScope()

    fun toggleFullScreen() {
        coroutineScope.launch {
            isFullScreen = !isFullScreen
        }
    }

    Column(
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            BasicTextField2(
                state = enterContent,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
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
            IconButton(
                onClick = { toggleFullScreen() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(imageVector = Icons.Default.OpenInFull, contentDescription = null)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.size(48.dp))
            InputActionView()
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }
    }
}

@Composable
private fun InputActionView(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(100.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Mic, contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.PhotoCamera, contentDescription = null)
        }
    }
}

@ThemePreviews
@Composable
private fun InputView_Preview() {
    EasyAITheme {
        InputView(modifier = Modifier)
    }
}