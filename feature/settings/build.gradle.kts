plugins {
    id("easy.android.library")
    id("easy.android.library.compose")
    id("easy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.easy.ai.feature.settings"
    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.navigation)
}