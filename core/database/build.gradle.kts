@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    //    alias(easy.plugins.android.compose.library)
//    alias(easy.plugins.hilt)
//    alias(easy.plugins.jacoco)
    id("org.easy.android.library.compose")
    id("org.easy.hilt")
    id("org.easy.jacoco")
    alias(libs.plugins.ksp)
}

android {
    namespace = "org.easy.ai.database"
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(projects.core.model)

    androidTestImplementation(projects.core.testing)
}