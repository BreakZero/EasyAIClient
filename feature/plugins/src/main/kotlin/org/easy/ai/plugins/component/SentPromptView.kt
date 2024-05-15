package org.easy.ai.plugins.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import org.easy.ai.plugins.multimodal.PromptContent
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme
import org.easy.ai.system.ui.localDim

@Composable
internal fun SentPromptView(
    modifier: Modifier = Modifier,
    promptContent: PromptContent
) {
    val shape = RoundedCornerShape(
        topEnd = MaterialTheme.localDim.default,
        bottomEnd = MaterialTheme.localDim.spaceSmall,
        bottomStart = MaterialTheme.localDim.spaceSmall,
        topStart = MaterialTheme.localDim.spaceSmall
    )
    val background =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        BoxWithConstraints {
            Card(
                modifier = Modifier
                    .widthIn(MaterialTheme.localDim.default, maxWidth * 0.9f)
                    .padding(end = MaterialTheme.localDim.spaceSmall),
                shape = shape,
                colors = background
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.localDim.spaceExtraSmall
                    )
                ) {
                    promptContent.images?.let {
                        ImageGridView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9F)
                                .clip(RoundedCornerShape(12.dp)),
                            images = it.map { it.asImageBitmap() }
                        )
                    }
                    Text(text = promptContent.prompt, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
internal fun AiResultContentView(
    modifier: Modifier = Modifier,
    textContent: String,
    inProgress: Boolean
) {
    val shape = RoundedCornerShape(
        topEnd = MaterialTheme.localDim.spaceSmall,
        bottomEnd = MaterialTheme.localDim.spaceSmall,
        bottomStart = MaterialTheme.localDim.spaceSmall,
        topStart = MaterialTheme.localDim.default
    )
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Icon(imageVector = Icons.Default.Computer, contentDescription = null)
        BoxWithConstraints {
            Card(
                modifier = Modifier
                    .widthIn(MaterialTheme.localDim.default, maxWidth * 0.9f)
                    .padding(top = MaterialTheme.localDim.spaceSmall, start = 12.dp),
                shape = shape
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.localDim.spaceExtraSmall
                    )
                ) {
                    Text(text = textContent, style = MaterialTheme.typography.titleLarge)
                    if (!inProgress) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.weight(1.0F))
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SentPromptView_Preview() {
    EasyAITheme {
//        AiResultContentView(modifier = Modifier, "hello world", inProgress = false)
        SentPromptView(promptContent = PromptContent("hello world"))
    }
}