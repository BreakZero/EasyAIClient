package org.easy.ai.system.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class Dimensions(
    val default: Dp = Dp.Hairline,
    val spaceXXSmall: Dp = 2.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceExtraLarge: Dp = 64.dp,
    val spaceXXLarge: Dp = 128.dp,
    val spaceXXXLarge: Dp = 256.dp,
    val space12: Dp = 12.dp,
    val space24: Dp = 24.dp
)

internal val LocalDim = staticCompositionLocalOf { Dimensions() }

val MaterialTheme.localDim: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDim.current


