pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("easy") {
            from(files("./build-logic/building.versions.toml"))
        }
    }
}

plugins {
    id("com.gradle.enterprise") version ("3.16.2")
}

//gradleEnterprise {
//    buildScan {
//        termsOfServiceUrl = "https://gradle.com/terms-of-service"
//        termsOfServiceAgree = "yes"
//        publishAlways()
//    }
//}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "EasyAI"
include(":app")
include(":core:system-ui")
include(":core:data")
include(":core:domain")
include(":feature:settings")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:model")
include(":feature:chat")
include(":core:network")
include(":feature:multimodal")
include(":benchmark")
