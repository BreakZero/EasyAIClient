package org.easy.ai.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.easy.ai.data.model.ChatUiModel
import org.easy.ai.system.ui.localDim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatItemContainer(
    modifier: Modifier = Modifier,
    chat: ChatUiModel,
    isSelected: Boolean,
    onItemClick: (ChatUiModel) -> Unit,
    onDelete: () -> Unit
) {
    val colors = if (isSelected) {
        ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    } else {
        ListItemDefaults.colors()
    }

    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            false
        }
    )

    SwipeToDismissBox(
        modifier = modifier.clip(RoundedCornerShape(MaterialTheme.localDim.spaceSmall)),
        state = swipeToDismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(MaterialTheme.localDim.spaceSmall))
                .clickable { onItemClick(chat) },
            headlineContent = { Text(text = chat.name) },
            colors = colors
        )
    }
}