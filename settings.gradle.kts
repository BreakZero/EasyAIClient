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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "EasyAI"
include(":app")
include(":core:system-ui")
include(":core:data")
include(":feature:settings")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:model")
include(":feature:chat")
