package org.easy.gemini.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.easy.gemini.feature.home.components.HomeScreenDrawer

@Composable
fun HomeContentRouter() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val message by homeViewModel.message.collectAsStateWithLifecycle()
    HomeScreenDrawer(message = message, onMessageChanged = homeViewModel::onMessageChanged)
}

