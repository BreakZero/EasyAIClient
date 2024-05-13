package org.easy.ai.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.common.ObserveAsEvents
import org.easy.ai.feature.settings.components.AiModelListDialog
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.R

@Composable
fun SettingsRoute(navigateToAiModels: () -> Unit, popBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = settingsViewModel.navigationEvents) {
        when (it) {
            SettingsEvent.NavigateToAiModelManager -> navigateToAiModels()
            SettingsEvent.NavigateToAbout -> {}
            else -> Unit
        }
    }

    SettingsScreen(
        settingsUiState,
        popBack = popBack,
        onEvent = settingsViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SettingsScreen(
    settingsUiState: SettingsUiState,
    popBack: () -> Unit,
    onEvent: (SettingsEvent) -> Unit
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
            }, title = { Text(text = stringResource(id = R.string.core_system_ui_text_settings)) })
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
                        onEvent(SettingsEvent.NavigateToAiModelManager)
                    }),
                headlineContent = {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = stringResource(id = R.string.core_system_ui_ai_model_manager)
                    )
                },
                trailingContent = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        onEvent(SettingsEvent.OpenSelector)
                    }),
                headlineContent = {
                    Column {
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = stringResource(id = R.string.core_system_ui_default_chat_model)
                        )
                        Text(
                            style = MaterialTheme.typography.labelSmall,
                            text = stringResource(id = R.string.core_system_ui_the_ai_using_for_chat)
                        )
                    }
                },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = settingsUiState.defaultChatAi?.name.orEmpty())
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                    }
                }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(SettingsEvent.NavigateToAbout) },
                headlineContent = {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = stringResource(R.string.core_system_ui_about_easyai)
                    )
                }
            )
        }

        if (settingsUiState.showAiSelector) {
            AiModelListDialog(
                default = settingsUiState.defaultChatAi,
                onDismiss = { onEvent(SettingsEvent.CloseSelector) },
                onSelected = {
                    onEvent(SettingsEvent.OnChatAiChanged(it))
                }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun SettingsScreen_Preview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        SettingsScreen(
            settingsUiState = SettingsUiState(),
            popBack = {},
            onEvent = {}
        )
    }
}