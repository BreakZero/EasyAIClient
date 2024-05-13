package org.easy.ai.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.system.ui.R
import org.easy.ai.system.ui.localDim

internal enum class DrawerState {
    Open,
    Closed
}

@Composable
internal fun ChatDrawer(
    modifier: Modifier = Modifier,
    chats: List<ChatUiModel>?,
    defaultChat: ChatUiModel? = null,
    onChatSelected: (ChatUiModel?) -> Unit,
    onChatDelete: (ChatUiModel) -> Unit,
    onPluginsClick: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    var selectedChat by remember(defaultChat) {
        mutableStateOf(defaultChat)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .weight(1.0f)
        ) {
            item {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedChat = null
                            onChatSelected(null)
                        },
                    headlineContent = { Text(text = stringResource(id = R.string.core_system_ui_new_chat)) }
                )
            }
            chats?.let {
                items(it) { chat ->
                    ChatItemContainer(
                        chat = chat,
                        isSelected = selectedChat == chat,
                        onItemClick = {
                            selectedChat = chat
                            onChatSelected(chat)
                        },
                        onDelete = { onChatDelete(chat) }
                    )
                }
            }
        }
        ListItem(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(MaterialTheme.localDim.spaceSmall))
                .clickable(onClick = onPluginsClick),
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.core_system_ui_plugins),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        HorizontalDivider()
        ListItem(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(MaterialTheme.localDim.spaceSmall))
                .clickable(onClick = onSettingsClicked),
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.core_system_ui_text_settings),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            leadingContent = {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            },
            trailingContent = {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        )
    }
}