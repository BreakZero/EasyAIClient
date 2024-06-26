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
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:model")
include(":core:network")
include(":benchmark")
include(":core:testing")

include(":feature:chat")
include(":feature:settings")
include(":feature:plugins")
include(":core:datastore-proto")
