@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.library.compose)
    alias(easy.plugins.android.hilt)
    alias(easy.plugins.android.library.jacoco)
}

android {
    namespace = "org.easy.ai.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
