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
import org.easy.ai.system.theme.ThemePreviews
import org.easy.ai.system.ui.R

@Composable
fun SettingsRoute(
    navigateToAiModels: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = settingsViewModel.navigationEvents) {
        when (it) {
            is SettingsEvent.ToAiModelManager -> navigateToAiModels()
            else -> Unit
        }
    }

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
            }, title = { Text(text = stringResource(id = R.string.text_settings)) })
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
                        onEvent(SettingsEvent.ToAiModelManager)
                    }),
                headlineContent = {
                    Column {
                        Text(
                            style = MaterialTheme.typography.titleMedium,
                            text = stringResource(id = R.string.text_ai_model_manager)
                        )
                        Text(
                            style = MaterialTheme.typography.labelSmall,
                            text = stringResource(id = R.string.text_activated_ai)
                        )
                    }
                },
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = settingsUiState.activatedAiModel?.name.orEmpty())
                        Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
                    }
                }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text(text = "About EasyAI")
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
            onEvent = {}
        )
    }
}