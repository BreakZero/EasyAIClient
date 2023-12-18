package org.easy.gemini.feature.home.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.easy.gemini.feature.home.model.Message

@Composable
internal fun MessageItemView(message: Message) {
    Card(modifier = Modifier) {
        Text(text = message.content)
    }
}