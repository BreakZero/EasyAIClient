@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("easy.android.library")
    id("easy.android.library.compose")
    id("easy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.easy.ai.plugins"
}

dependencies {
    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.navigation)
}