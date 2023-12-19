import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.feature.hilt)
}

android {
    namespace = "org.easy.gemini.feature.home"
    defaultConfig {
        val localProperties = localProperties()
        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${localProperties.getProperty("gemini_api_key")}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

fun localProperties(): Properties {
    val properties: Properties = Properties().apply {
        val localFile = rootProject.file("local.properties")
        if (localFile.isFile) {
            InputStreamReader(FileInputStream(localFile), Charsets.UTF_8).use {
                this.load(it)
            }
        }
    }
    return properties
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.generativeai)
    implementation(libs.androidx.hilt.navigation.compose)
}