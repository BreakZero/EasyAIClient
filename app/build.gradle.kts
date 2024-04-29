import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("org.easy.android.application.compose")
    id("org.easy.jacoco")
    id("org.easy.hilt")
}

android {
    namespace = "org.easy.ai.client"

    defaultConfig {
        applicationId = "org.easy.ai.client"
        targetSdk = 34
        versionCode = 100000011
        versionName = "v0.1.0"
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
    buildFeatures {
        buildConfig = true
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
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.lifecycle.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(projects.core.systemUi)
    implementation(projects.core.data)
    implementation(projects.feature.chat)
    implementation(projects.feature.settings)
    implementation(projects.feature.plugins)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.timber)
}
