@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.easy.android.library.compose")
    id("org.easy.hilt")
}

android {
    namespace = "org.easy.ai.chat"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.systemUi)

    implementation(libs.generativeai)
    implementation(libs.androidx.hilt.navigation.compose)
}