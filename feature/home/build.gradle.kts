@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.feature.hilt)
}

android {
    namespace = "org.easy.gemini.feature.home"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.generativeai)
    implementation(libs.androidx.hilt.navigation.compose)
}