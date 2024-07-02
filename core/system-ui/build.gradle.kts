plugins {
    id("easy.android.library")
    id("easy.android.library.compose")
}

android {
    namespace = "org.easy.ai.system.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.compose.activity)
    implementation(libs.bundles.compose.android.bundle)

    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)

    implementation(libs.coil.kt.compose)
}