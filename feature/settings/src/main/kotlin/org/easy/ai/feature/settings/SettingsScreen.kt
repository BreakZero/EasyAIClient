package org.easy.ai.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.ai.feature.settings.components.ApiKeyEditorDialog
import org.easy.ai.model.UserData
import org.easy.ai.system.theme.ThemePreviews

@Composable
fun SettingsRoute() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val userDataUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsScreen(
        userDataUiState,
        onModelNameChanged = settingsViewModel::onModelNameChanged,
        applyApiKeyChanged = settingsViewModel::setApiKey
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SettingsScreen(
    userData: UserData,
    onModelNameChanged: (String) -> Unit,
    applyApiKeyChanged: (String) -> Unit
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
        var expanded by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = {
                    Text(text = "Choose Model Platform")
                },
                trailingContent = {
                    Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
                }
            )
            ListItem(
                headlineContent = {
                    Text(text = "Choose Your API KEY")
                },
                trailingContent = {
                    Row(
                        modifier = Modifier
                            .clickable {
                                showDialog = !showDialog
                            },
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
        }
        if (showDialog) {
            var apiKey by remember {
                mutableStateOf(userData.apiKey)
            }
            ApiKeyEditorDialog(
                apiKey = apiKey,
                onApiKeyChanged = { apiKey = it },
                applyApiKeyChanged = {
                    applyApiKeyChanged(apiKey)
                },
                onDismiss = {
                    showDialog = false
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
            userData = UserData("", ""),
            onModelNameChanged = {},
            applyApiKeyChanged = {}
        )
    }
}