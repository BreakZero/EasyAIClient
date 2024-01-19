package org.easy.ai.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.easy.ai.model.ChatMessage
import org.easy.ai.model.Participant
import org.easy.ai.system.ui.localDim

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
            Spacer(modifier = Modifier.width(MaterialTheme.localDim.spaceSmall))
        }
        val background =
            if (isUser) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            else CardDefaults.cardColors()
        val shape = if (isUser) RoundedCornerShape(
            topEnd = MaterialTheme.localDim.default,
            bottomEnd = MaterialTheme.localDim.spaceSmall,
            bottomStart = MaterialTheme.localDim.spaceSmall,
            topStart = MaterialTheme.localDim.spaceSmall
        ) else RoundedCornerShape(
            topEnd = MaterialTheme.localDim.spaceSmall,
            bottomEnd = MaterialTheme.localDim.spaceSmall,
            bottomStart = MaterialTheme.localDim.spaceSmall,
            topStart = MaterialTheme.localDim.default
        )
        Row {
            BoxWithConstraints {
                Card(
                    modifier = Modifier
                        .widthIn(MaterialTheme.localDim.default, maxWidth * 0.9f)
                        .padding(top = MaterialTheme.localDim.spaceSmall),
                    colors = background,
                    shape = shape
                ) {
                    if (message.isPending) {
                        CircularProgressIndicator()
                    } else {
                        SelectionContainer(
                            modifier = Modifier.padding(MaterialTheme.localDim.spaceSmall)
                        ) {
                            Text(text = message.text)
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(MaterialTheme.localDim.spaceSmall))
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
        }
    }
}