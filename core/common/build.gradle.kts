@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
//    alias(easy.plugins.android.compose.library)
//    alias(easy.plugins.hilt)
//    alias(easy.plugins.jacoco)
    id("org.easy.android.library.compose")
    id("org.easy.hilt")
    id("org.easy.jacoco")
}

android {
    namespace = "org.easy.ai.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
