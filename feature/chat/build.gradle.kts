@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.feature.hilt)
}

android {
    namespace = "org.easy.ai.chat"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)

    implementation(libs.generativeai)
    implementation(libs.androidx.hilt.navigation.compose)
}