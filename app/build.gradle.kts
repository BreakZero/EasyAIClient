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
    namespace = "org.easy.gemini.client"

    val localProperties = localProperties()
    defaultConfig {
        applicationId = "org.easy.gemini.client"
        versionCode = 1000000
        versionName = "v1.0.0"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${localProperties.getProperty("gemini_api_key")}\""
        )
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
        release {
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

    implementation(projects.core.systemUi)
    implementation(projects.feature.home)
}