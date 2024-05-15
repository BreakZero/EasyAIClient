package org.easy.ai.plugins.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
internal fun ImageGridView(
    modifier: Modifier = Modifier,
    images: List<ImageBitmap>
) {
    when (images.size) {
        1 -> {
            Image(
                modifier = modifier,
                bitmap = images.first(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        2 -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                images.onEach {
                    Image(
                        modifier = Modifier.weight(1.0f),
                        bitmap = it,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        3 -> {
            Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Image(
                    modifier = Modifier.weight(1.0f),
                    bitmap = images.first(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (index in 1 until 3) {
                        Image(
                            modifier = Modifier.weight(1.0f),
                            bitmap = images[index],
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }

        4 -> {
            Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (index in 0..1) {
                        Image(
                            modifier = Modifier.weight(1.0f),
                            bitmap = images[index],
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (index in 2..3) {
                        Image(
                            modifier = Modifier.weight(1.0f),
                            bitmap = images[index],
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}