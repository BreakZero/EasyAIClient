plugins {
//    alias(easy.plugins.android.library)
//    alias(easy.plugins.hilt)
    id("easy.android.library")
    id("easy.android.hilt")
}

android {
    namespace = "org.easy.ai.testing"
}

dependencies {

    api(libs.androidx.test.core)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.roborazzi)
    api(libs.robolectric.shadows)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}