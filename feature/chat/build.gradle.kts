plugins {
    id("easy.android.library")
    id("easy.android.library.compose")
    id("easy.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.easy.ai.chat"
    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.systemUi)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.navigation)
}