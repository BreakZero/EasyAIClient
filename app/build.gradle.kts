import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.application.compose)
    alias(easy.plugins.android.application.jacoco)
    id("jacoco")
    alias(easy.plugins.android.hilt)
}

android {
    namespace = "org.easy.ai.client"

    defaultConfig {
        applicationId = "org.easy.ai.client"
        versionCode = 1000000
        versionName = "v1.0.0"
    }
    val keyProperties = keyStoreProperties()
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(keyProperties.getProperty("storeFile"))
            storePassword = keyProperties.getProperty("storePassword")
            keyAlias = keyProperties.getProperty("keyAlias")
            keyPassword = keyProperties.getProperty("keyPassword")
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = getByName("debug").signingConfig
            isMinifyEnabled = false
            matchingFallbacks += listOf("release")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    packaging {
        resources {
            excludes.add("META-INF/versions/9/previous-compilation-data.bin")
        }
    }
}

fun keyStoreProperties(): Properties {
    val properties = Properties()
    val keyPropertiesFile = rootProject.file("keystore/keystore.properties")

    if (keyPropertiesFile.isFile) {
        InputStreamReader(FileInputStream(keyPropertiesFile), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}

dependencies {
    implementation(libs.generativeai)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.feature.chat)
    implementation(projects.feature.settings)
}