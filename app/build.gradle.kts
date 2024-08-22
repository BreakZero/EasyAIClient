import org.easy.mobile.convention.configureFlavors
import org.easy.mobile.convention.getPropertiesByFile

plugins {
    id("easy.android.application")
    id("easy.android.application.compose")
    id("easy.android.jacoco")
    id("easy.android.hilt")
}

android {
    namespace = "org.easy.ai.client"

    val versionInfo = project.getPropertiesByFile("configs/version_info.properties")

    defaultConfig {
        applicationId = "org.easy.ai.client"
        versionCode = versionInfo.getProperty("versionCode", "1").toInt()
        versionName = System.getenv("VERSION_NAME") ?: versionInfo.getProperty("versionName", "v1.0.0")
    }
    val keyProperties = project.getPropertiesByFile("configs/keystore.properties")
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
    configureFlavors(this)
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

    implementation(libs.timber)
}
