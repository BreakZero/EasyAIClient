package org.easy.ai.chat

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
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
    navigateToPlugins: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chatUiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()
    val contentUiState by chatViewModel.chatContentUiState.collectAsStateWithLifecycle()
    ObserveAsEvents(flow = chatViewModel.navigationEvents, onEvent = { event ->
        when (event) {
            is ChatEvent.OnSettingsClicked -> navigateToSettings()
            is ChatEvent.OnPluginsClicked -> navigateToPlugins()
            else -> Unit
        }
    })
    ChatScreen(
        chatUiState = chatUiState,
        contentUiState = contentUiState,
        onEvent = chatViewModel::onEvent
    )
}

@Composable
internal fun ChatScreen(
    chatUiState: ChatUiState,
    contentUiState: ChattingUiState,
    onEvent: (ChatEvent) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
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
            chats = if (chatUiState is ChatUiState.Initialed) contentUiState.chats else null,
            defaultChat = contentUiState.selectedChat,
            onChatSelected = {
                onEvent(ChatEvent.SelectedChat(it))
            },
            onChatDelete = {
                onEvent(ChatEvent.OnDeleteChat(it.chatId))
            },
            onPluginsClick = { onEvent(ChatEvent.OnPluginsClicked) },
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
            contentUiState = contentUiState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatContent(
    modifier: Modifier = Modifier,
    chatUiState: ChatUiState,
    contentUiState: ChattingUiState,
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
                },
                actions = {
                    if (contentUiState.selectedChat == null && chatUiState is ChatUiState.Initialed) {
                        IconButton(onClick = {
                            onEvent(ChatEvent.OnSaveChat)
                        }) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (chatUiState is ChatUiState.Initialed) {
                MessageInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.localDim.spaceSmall),
                    onMessageSend = { onEvent(ChatEvent.OnMessageSend(it)) }
                )
            }
        }
    ) { paddings ->
        when (chatUiState) {
            is ChatUiState.Initialed -> {
                val chatListState = rememberLazyListState()
                LaunchedEffect(
                    key1 = contentUiState.chatHistory,
                    key2 = contentUiState.pendingMessage
                ) {
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
                    items(contentUiState.chatHistory) { message ->
                        ChatMessageItemView(
                            modifier = Modifier.fillMaxWidth(),
                            message = message
                        )
                    }
                    contentUiState.pendingMessage?.let {
                        item {
                            ChatMessageItemView(message = it)
                        }
                    }
                }
            }

            ChatUiState.NoApiSetup -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddings),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.localDim.spaceMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            text = stringResource(id = UiR.string.text_no_api_key_for_chat)
                        )
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
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun MessageInput(
    modifier: Modifier = Modifier,
    onMessageSend: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.localDim.spaceSmall),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicTextField2(
            modifier = Modifier
                .weight(1.0f),
            state = textFieldState,
            decorator = @Composable {
                val interactionSource = remember { MutableInteractionSource() }
                OutlinedTextFieldDefaults.DecorationBox(
                    value = textFieldState.text.toString(),
                    innerTextField = it,
                    enabled = true,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = TextFieldDefaults.colors(),
                            shape = RoundedCornerShape(MaterialTheme.localDim.spaceMedium)
                        )
                    }
                )
            }
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            onClick = {
                onMessageSend(textFieldState.text.toString())
                textFieldState.clearText()
            },
            enabled = textFieldState.text.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null
            )
        }
    }
}