package org.easy.ai.chat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.easy.ai.chat.component.ChatDrawer
import org.easy.ai.chat.component.ChatMessageItemView
import org.easy.ai.chat.component.DrawerState
import org.easy.ai.common.ObserveAsEvents
import org.easy.ai.system.ui.localDim
import org.easy.ai.system.ui.R as UiR

private val DrawerWidth = 300.dp

@Composable
internal fun ChatRoute(
    navigateToMultiModal: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatUiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()
    ObserveAsEvents(flow = chatViewModel.navigationEvents, onEvent = { event ->
        when(event) {
            is ChatEvent.OnSettingsClicked -> navigateToSettings()
            is ChatEvent.OnMultiModalClicked -> navigateToMultiModal()
            else -> Unit
        }
    })
    ChatScreen(chatUiState = chatUiState, onEvent = chatViewModel::onEvent)
}

@Composable
internal fun ChatScreen(
    chatUiState: ChatUiState,
    onEvent: (ChatEvent) -> Unit
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
                focusManager.clearFocus()
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
        ChatDrawer(
            chats = (chatUiState as? ChatUiState.Initialed)?.chats,
            defaultChat = (chatUiState as? ChatUiState.Initialed)?.currentChat,
            hasSetup = chatUiState is ChatUiState.Initialed,
            onChatSelected = {
                onEvent(ChatEvent.SelectedChat(it))
            },
            onTextGeneratorClicked = {},
            onMultiModalClicked = {
                onEvent(ChatEvent.OnMultiModalClicked)
            },
            onSettingsClicked = {
                onEvent(ChatEvent.OnSettingsClicked)
            }
        )
        val draggableState = rememberDraggableState(onDelta = { dragAmount ->
            coroutineScope.launch {
                translationX.snapTo(translationX.value + dragAmount)
            }
        })
        val decay = rememberSplineBasedDecay<Float>()
        ChatContent(
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
            chatUiState = chatUiState,
            onDrawerClicked = ::toggleDrawerState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatContent(
    modifier: Modifier = Modifier,
    chatUiState: ChatUiState,
    onDrawerClicked: () -> Unit,
    onEvent: (ChatEvent) -> Unit
) {
    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.testTag("chat_menu"),
                        onClick = onDrawerClicked
                    ) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            if (chatUiState is ChatUiState.Initialed) {
                MessageInput(
                    onMessageSend = { onEvent(ChatEvent.OnMessageSend(it)) }
                )
            }
        }
    ) { paddings ->
        when (chatUiState) {
            is ChatUiState.Configuration -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = MaterialTheme.localDim.spaceMedium),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Oops, Please config your environment first")
                        TextButton(onClick = {
                            onEvent(ChatEvent.OnSettingsClicked)
                        }) {
                            Text(
                                text = stringResource(id = UiR.string.action_go_to_settings),
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                    }
                }
            }

            is ChatUiState.Initialed -> {
                val chatListState = rememberLazyListState()
                LaunchedEffect(key1 = chatUiState.chatHistory) {
                    chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddings)
                        .padding(horizontal = MaterialTheme.localDim.spaceMedium)
                        .padding(bottom = MaterialTheme.localDim.spaceSmall),
                    state = chatListState,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceSmall)
                ) {
                    items(chatUiState.chatHistory) { message ->
                        ChatMessageItemView(
                            modifier = Modifier.fillMaxWidth(),
                            message = message
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun MessageInput(
    onMessageSend: (String) -> Unit
) {
    var userMessage by rememberSaveable {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.localDim.spaceSmall),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1.0f),
            value = userMessage,
            onValueChange = { userMessage = it }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                onMessageSend(userMessage)
                userMessage = ""
            },
            enabled = userMessage.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null
            )
        }
    }
}