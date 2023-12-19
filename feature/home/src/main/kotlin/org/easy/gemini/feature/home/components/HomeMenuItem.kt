package org.easy.gemini.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeMenuItemView(
    modifier: Modifier = Modifier,
    item: String,
    onItemClick: () -> Unit,
    endingIcon: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.clickable {
            onItemClick()
        }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item)
        Spacer(modifier = Modifier.weight(1.0f))
        endingIcon()
    }
}