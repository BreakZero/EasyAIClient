package org.easy.gemini.feature.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import org.easy.gemini.feature.home.components.HomeScreenDrawer

@Composable
fun HomeContentRouter() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    HomeScreenDrawer()
}

