@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.easy.android.library.compose")
    id("org.easy.hilt")
}

android {
    namespace = "org.easy.ai.plugins"
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.common)
}