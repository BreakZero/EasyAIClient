package org.easy.gemini.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.gemini.feature.settings.components.ActionItemView
import org.easy.gemini.feature.settings.components.ApiKeyEditorDialog
import org.easy.gemini.model.UserData

@Composable
fun SettingsRoute() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val userDataUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    SettingsScreen(userDataUiState, onModelNameChanged = settingsViewModel::onModelNameChanged)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun SettingsScreen(
    userData: UserData,
    onModelNameChanged: (String) -> Unit
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
                .padding(horizontal = 16.dp)
        ) {
            ActionItemView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                label = "Set up your API KEY",
                trailing = {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
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
            ActionItemView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                label = "Choose Model Name",
                trailing = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Row {
                            Text(
                                text = userData.modelName,
                                modifier = Modifier
                                    .clickable { expanded = true },
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text(text = "gemini-pro") },
                                onClick = {
                                    onModelNameChanged("gemini-pro")
                                    expanded = false
                                })
                            DropdownMenuItem(
                                text = { Text(text = "gemini-pro-vision") },
                                onClick = {
                                    onModelNameChanged("gemini-pro-vision")
                                    expanded = false
                                })
                        }
                    }
                }
            )
        }
        if (showDialog) {
            ApiKeyEditorDialog {

            }
        }
    }
}