package org.easy.gemini.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart

@Composable
internal fun MessageItemView(
    modifier: Modifier = Modifier,
    message: Content
) {
    val isUser = message.role == "user"
    Row(
        modifier = modifier,
        horizontalArrangement = if (!isUser) Arrangement.Start else Arrangement.End
    ) {
        if (!isUser) {
            Icon(imageVector = Icons.Default.AddIcCall, contentDescription = null)
        }
        Card(modifier = Modifier) {
            Text(modifier = Modifier.padding(8.dp), text = message.parts.filterIsInstance<TextPart>()
                .joinToString { it.text })
        }
        if (isUser) {
            Icon(imageVector = Icons.Default.ContactPage, contentDescription = null)
        }
    }
}