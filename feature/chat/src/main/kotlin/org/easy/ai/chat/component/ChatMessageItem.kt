package org.easy.ai.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant

@Composable
internal fun ChatMessageItemView(
    modifier: Modifier = Modifier,
    message: ChatMessage
) {
    val isUser = message.participant == Participant.USER
    Row(
        modifier = modifier,
        horizontalArrangement = if (!isUser) Arrangement.Start else Arrangement.End
    ) {
        if (!isUser) {
            Icon(imageVector = Icons.Default.Computer, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        val background =
            if (isUser) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else CardDefaults.cardColors()
        val shape = if (isUser) RoundedCornerShape(
            topEnd = 0.dp,
            bottomEnd = 8.dp,
            bottomStart = 8.dp,
            topStart = 8.dp
        ) else RoundedCornerShape(
            topEnd = 8.dp,
            bottomEnd = 8.dp,
            bottomStart = 8.dp,
            topStart = 0.dp
        )
        Card(modifier = Modifier.padding(top = 8.dp), colors = background, shape = shape) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = message.text
            )
        }
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
        }
    }
}