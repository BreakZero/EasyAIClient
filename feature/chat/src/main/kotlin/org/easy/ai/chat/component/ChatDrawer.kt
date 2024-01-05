package org.easy.ai.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.easy.ai.data.model.AiChat

internal enum class DrawerState {
    Open, Closed
}

@Composable
internal fun ChatDrawer(
    modifier: Modifier = Modifier,
    chats: List<AiChat>?,
    defaultChat: AiChat? = null,
    onSelectedChat: (AiChat) -> Unit,
    onSettingsClicked: () -> Unit
) {
    var selectedChat = remember {
        mutableStateOf(defaultChat)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            item {
                ListItem(headlineContent = { Text(text = "New Chat") })
            }
            chats?.let {
                items(it) { chat ->
                    ListItem(modifier = Modifier.fillMaxWidth(), headlineContent = { Text(text = chat.name) })
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onSettingsClicked)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "D&J")
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }
    }

}