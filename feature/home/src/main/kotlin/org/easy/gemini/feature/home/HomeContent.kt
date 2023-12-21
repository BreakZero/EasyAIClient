package org.easy.gemini.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.gemini.feature.home.components.HomeScreenDrawer
import org.easy.gemini.feature.home.components.MessageItemView

@Composable
fun HomeContentRouter(
    navigateToSettings: () -> Unit
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    HomeScreenDrawer(
        historyChats = listOf("What is Compose", "Please introduce yourself"),
        onMessageChanged = homeViewModel::onMessageChanged,
        onMessageSent = homeViewModel::sendMessage,
        homeUiState = homeUiState,
        navigateToSettings = navigateToSettings
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    onMessageChanged: (String) -> Unit,
    onDrawerClicked: () -> Unit,
    onMessageSent: () -> Unit
) {
    Scaffold(
        modifier = modifier.imePadding(),
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
    ) { padding ->
        when (homeUiState) {
            is HomeUiState.Initialed -> {
                val chatListState = rememberLazyListState()
                LaunchedEffect(key1 = homeUiState.history) {
                    chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .weight(1.0f),
                        state = chatListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(homeUiState.history) { message ->
                            MessageItemView(modifier = Modifier.fillMaxWidth(), message = message)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1.0f),
                            value = homeUiState.message,
                            onValueChange = onMessageChanged,
                            shape = RoundedCornerShape(50.dp),
                            trailingIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = onMessageSent,
                            enabled = homeUiState.message.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            is HomeUiState.NeedToSetup -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Please setup your api key first")
                }
            }

            else -> Unit
        }
    }
}
