package org.easy.ai.feature.home

sealed interface HomeEvent {
    data class SendMessage(val content: String): HomeEvent
    data object ToggleDrawer: HomeEvent
    data object SettingsClick: HomeEvent
}
