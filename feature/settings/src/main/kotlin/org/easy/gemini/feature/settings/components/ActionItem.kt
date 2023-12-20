package org.easy.gemini.feature.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ActionItemView(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    trailingIcon: @Composable RowScope.() -> Unit = {}
) {
    Row(modifier = modifier) {
        Text(text = label)
        Spacer(modifier = Modifier.weight(1.0f))
        if (value.isNotBlank()) {
            Text(text = value)
        }
        trailingIcon()
    }
}