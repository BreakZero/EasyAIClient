package org.easy.gemini.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            Icon(imageVector = Icons.Default.Computer, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        val background = if (isUser) CardDefaults.cardColors(containerColor = Color.Green) else CardDefaults.cardColors()
        Card(modifier = Modifier, colors = background) {
            Text(modifier = Modifier.padding(8.dp), text = message.parts.filterIsInstance<TextPart>()
                .joinToString { it.text })
        }
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
        }
    }
}