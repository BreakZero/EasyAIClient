package org.easy.ai.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.easy.ai.model.MessageType
import org.easy.ai.model.Participant
import org.easy.ai.system.ui.localDim

@Composable
internal fun ChatMessageItemView(modifier: Modifier = Modifier, message: ChatMessage) {
    val isUser = message.participant == Participant.USER
    if (isUser) {
        UserMessage(modifier = modifier, message = message)
    } else {
        ModelMessage(modifier = modifier, message = message)
    }
}

@Composable
private fun ModelMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    val shape = RoundedCornerShape(
        topEnd = MaterialTheme.localDim.spaceSmall,
        bottomEnd = MaterialTheme.localDim.spaceSmall,
        bottomStart = MaterialTheme.localDim.spaceSmall,
        topStart = MaterialTheme.localDim.default
    )
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Icon(imageVector = Icons.Default.Computer, contentDescription = null)
        Spacer(modifier = Modifier.width(MaterialTheme.localDim.spaceExtraSmall))
        BoxWithConstraints {
            Card(
                modifier = Modifier
                    .widthIn(MaterialTheme.localDim.default, maxWidth * 0.9f)
                    .padding(top = MaterialTheme.localDim.spaceSmall),
                shape = shape
            ) {
                SelectionContainer(
                    modifier = Modifier.padding(MaterialTheme.localDim.spaceSmall)
                ) {
                    Text(text = message.content)
                }
            }
        }
    }
}

@Composable
private fun UserMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    val shape = RoundedCornerShape(
        topEnd = MaterialTheme.localDim.default,
        bottomEnd = MaterialTheme.localDim.spaceSmall,
        bottomStart = MaterialTheme.localDim.spaceSmall,
        topStart = MaterialTheme.localDim.spaceSmall
    )
    val background =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        BoxWithConstraints {
            Card(
                modifier = Modifier
                    .widthIn(MaterialTheme.localDim.default, maxWidth * 0.9f)
                    .padding(top = MaterialTheme.localDim.spaceSmall),
                colors = background,
                shape = shape
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.localDim.spaceExtraSmall
                    )
                ) {
                    if (message.type == MessageType.PENDING) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(MaterialTheme.localDim.spaceExtraSmall)
                                .size(MaterialTheme.localDim.spaceLarge)
                        )
                    }
                    SelectionContainer(
                        modifier = Modifier.padding(MaterialTheme.localDim.spaceSmall)
                    ) {
                        Text(text = message.content)
                    }
                }
            }
        }
        Icon(imageVector = Icons.Default.Person, contentDescription = null)
    }
}