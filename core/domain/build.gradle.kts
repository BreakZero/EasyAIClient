plugins {
//    alias(easy.plugins.android.compose.library)
//    alias(easy.plugins.hilt)
//    alias(easy.plugins.jacoco)
    id("easy.android.library")
    id("easy.android.hilt")
    id("easy.android.jacoco")
}

android {
    namespace = "org.easy.ai.domain"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.datastore)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}