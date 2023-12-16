package org.easy.gemini.feature.home.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreenDrawer(
    message: String,
    onMessageChanged: (String) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val coroutineScope = rememberCoroutineScope()
        var drawerState by remember {
            mutableStateOf(DrawerState.Closed)
        }
        val translationX = remember {
            Animatable(0f)
        }

        val drawerWidth = with(LocalDensity.current) {
            DrawerWidth.toPx()
        }
        translationX.updateBounds(0f, drawerWidth)

        val focusManager = LocalFocusManager.current

        fun toggleDrawerState() {
            coroutineScope.launch {
                if (drawerState == DrawerState.Open) {
                    translationX.animateTo(0f)
                } else {
                    translationX.animateTo(drawerWidth)
                }
                drawerState = if (drawerState == DrawerState.Open) {
                    DrawerState.Closed
                } else {
                    DrawerState.Open
                }
            }
        }

        HomeScreenDrawerContent(
            historyChats = emptyList(),
            onSelected = { /*TODO*/ },
            onSettingsClick = {}
        )

        val draggableState = rememberDraggableState(onDelta = { dragAmount ->
            coroutineScope.launch {
                translationX.snapTo(translationX.value + dragAmount)
            }
        })
        val decay = rememberSplineBasedDecay<Float>()

        HomeScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.translationX = translationX.value
                    val scale = lerp(
                        ScaleFactor(1.0f, 1.0f),
                        ScaleFactor(0.8f, 0.8f),
                        translationX.value / drawerWidth
                    )
                    this.scaleX = scale.scaleX
                    this.scaleY = scale.scaleY
                    val roundedCorners = lerp(
                        0.dp,
                        32.dp,
                        translationX.value / drawerWidth
                    )
                    this.shape = RoundedCornerShape(roundedCorners)
                    this.clip = true
                    this.shadowElevation = 32f
                }
                .draggable(
                    draggableState, Orientation.Horizontal,
                    onDragStarted = {
                        focusManager.clearFocus()
                    },
                    onDragStopped = { velocity ->
                        val targetOffsetX = decay.calculateTargetValue(
                            translationX.value,
                            velocity
                        )
                        coroutineScope.launch {
                            val actualTargetX = if (targetOffsetX > drawerWidth * 0.5) {
                                drawerWidth
                            } else {
                                0f
                            }
                            // checking if the difference between the target and actual is + or -
                            val targetDifference = (actualTargetX - targetOffsetX)
                            val canReachTargetWithDecay =
                                (targetOffsetX > actualTargetX && velocity > 0f &&
                                        targetDifference > 0f) ||
                                        (targetOffsetX < actualTargetX && velocity < 0 &&
                                                targetDifference < 0f)
                            if (canReachTargetWithDecay) {
                                translationX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            } else {
                                translationX.animateTo(actualTargetX, initialVelocity = velocity)
                            }
                            drawerState = if (actualTargetX == drawerWidth) {
                                DrawerState.Open
                            } else {
                                DrawerState.Closed
                            }
                        }
                    }
                ),
            message = message,
            onMessageChanged = onMessageChanged,
            onDrawerClicked = ::toggleDrawerState
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    message: String = "",
    onMessageChanged: (String) -> Unit,
    onDrawerClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = onDrawerClicked) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                    }
                }
            )
        },
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .background(Color.Cyan)
            ) {

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1.0f),
                    value = message,
                    onValueChange = onMessageChanged
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(onClick = {
                    println("===== $message")
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                }
            }
        }
    }
}

private val DrawerWidth = 300.dp

private enum class DrawerState {
    Open,
    Closed
}

@Composable
private fun HomeScreenDrawerContent(
    modifier: Modifier = Modifier,
    historyChats: List<String>,
    onSelected: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null)
            Text(text = "D&J")
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }
}