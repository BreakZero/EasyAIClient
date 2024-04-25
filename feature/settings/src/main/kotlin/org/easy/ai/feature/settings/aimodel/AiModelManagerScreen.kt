package org.easy.ai.feature.settings.aimodel

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.easy.ai.model.AiModel
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme
import org.easy.ai.system.ui.R

@Composable
internal fun AiModelManagerRoute() {
    AiModelManagerScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiModelManagerScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }, title = { Text(text = stringResource(id = R.string.text_supported_model)) })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(2, key = { it }) {
                AiModelItem(aiModel = AiModel.GEMINI) {
                    
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AiModelManager_preview() {
    EasyAITheme {
        AiModelManagerScreen()
    }
}