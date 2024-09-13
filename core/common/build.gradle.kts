plugins {
//    alias(easy.plugins.android.compose.library)
//    alias(easy.plugins.hilt)
//    alias(easy.plugins.jacoco)
    id("easy.android.library")
    id("easy.android.library.compose")
    id("easy.android.hilt")
    id("easy.android.jacoco")
}

android {
    namespace = "org.easy.ai.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.timber)
}
