@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.feature.hilt)
}

android {
    namespace = "org.easy.ai.feature.settings"
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.core.model)
}