@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(easy.plugins.android.library)
    alias(easy.plugins.android.hilt)
    alias(easy.plugins.android.library.jacoco)
}

android {
    namespace = "org.easy.gemini.data"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.model)
}