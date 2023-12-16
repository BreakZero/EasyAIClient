package org.easy.gemini.feature.home

sealed interface HomeEvent {
    data class SendMessage(val content: String): HomeEvent
    data object ToggleDrawer: HomeEvent
    data object SettingsClick: HomeEvent
}
