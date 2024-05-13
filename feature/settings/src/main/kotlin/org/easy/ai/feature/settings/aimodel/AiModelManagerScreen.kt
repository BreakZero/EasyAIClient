package org.easy.ai.feature.settings.aimodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.feature.settings.components.ApiKeyEditorDialog
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.EasyAITheme
import org.easy.ai.system.ui.R
import org.easy.ai.system.ui.localDim

@Composable
internal fun AiModelManagerRoute(popBack: () -> Unit) {
    val viewModel: AiModelManagerViewModel = hiltViewModel()
    val aiModels by viewModel.aiModels.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AiModelManagerScreen(aiModels, uiState, popBack, viewModel::eventHandling)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiModelManagerScreen(
    aiModels: List<AiModelUiModel>,
    uiState: AiModelUiState,
    popBack: () -> Unit,
    eventHandling: (AiModelUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = popBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }, title = { Text(text = stringResource(id = R.string.core_system_ui_supported_model)) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.localDim.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.localDim.spaceSmall)
        ) {
            items(aiModels, key = { it.aiModel }) { aiModelInfo ->
                AiModelItem(aiModelInfo = aiModelInfo) {
                    eventHandling(AiModelUiEvent.ClickEdit(it.aiModel, it.apiKey))
                }
            }
        }

        if (uiState.inEditModel) {
            ApiKeyEditorDialog(
                initialKey = uiState.selectedKey,
                aiModel = uiState.selectedAi,
                applyApiKeyChanged = { aiModel, apiKey ->
                    eventHandling(AiModelUiEvent.UpdateApiKey(aiModel, apiKey))
                },
                onDismiss = {
                    eventHandling(AiModelUiEvent.EditDone)
                }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AiModelManager_preview() {
    EasyAITheme {
        AiModelManagerScreen(emptyList(), AiModelUiState(), {}, {})
    }
}