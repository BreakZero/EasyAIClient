package org.easy.ai.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.feature.settings.components.ApiKeyEditorDialog
import org.easy.ai.feature.settings.components.ModelList
import org.easy.ai.model.AIModel
import org.easy.ai.system.theme.ThemePreviews

@Composable
fun SettingsRoute() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsScreen(
        settingsUiState,
        onEvent = settingsViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SettingsScreen(
    settingsUiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit
) {
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
            }, title = { Text(text = "Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        onEvent(SettingsEvent.ShowModelList)
                    }),
                headlineContent = {
                    Text(text = "Choose Model Platform")
                },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = settingsUiState.model.name)
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
                    }
                }
            )
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(SettingsEvent.ShowApiKeyEditor)
                    },
                headlineContent = {
                    Text(text = "Choose Your API KEY")
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "******")
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            )
            ListItem(
                modifier = Modifier.clickable { },
                headlineContent = {
                    Text(text = "Automatic chat save")
                },
                trailingContent = {
                    Switch(
                        checked = settingsUiState.isAutomaticSaveChat,
                        onCheckedChange = {
                            onEvent(SettingsEvent.AutomaticSaveChatChanged(it))
                        }
                    )
                }
            )
        }
        if (settingsUiState.isApiKeyEditorShowed) {
            ApiKeyEditorDialog(
                apiKey = settingsUiState.apiKey,
                applyApiKeyChanged = {
                    onEvent(SettingsEvent.SavedApiKey(it))
                },
                onDismiss = {
                    onEvent(SettingsEvent.HideApiKeyEditor)
                }
            )
        }
        if (settingsUiState.isModelListShowed) {
            ModelList(
                models = AIModel.values().toList(),
                default = settingsUiState.model,
                onDismiss = { onEvent(SettingsEvent.HideModelList) },
                onSelected = {
                    onEvent(SettingsEvent.SavedModel(it))
                })
        }
    }
}

@ThemePreviews
@Composable
private fun SettingsScreen_Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        SettingsScreen(
            settingsUiState = SettingsUiState(),
            onEvent = {}
        )
    }
}