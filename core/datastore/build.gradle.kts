plugins {
//    alias(easy.plugins.android.compose.library)
//    alias(easy.plugins.hilt)
//    alias(easy.plugins.jacoco)
    id("easy.android.library")
    id("easy.android.library.compose")
    id("easy.android.hilt")
    id("easy.android.jacoco")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "org.easy.ai.datastore"
    defaultConfig {
        consumerProguardFile("consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    api(projects.core.datastoreProto)

    implementation(libs.androidx.dataStore)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}