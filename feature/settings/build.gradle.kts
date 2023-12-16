@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.feature.hilt)
}

android {
    namespace = "org.easy.gemini.feature.settings"
}
