import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidx.room) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
}

//subprojects {
//    if (this.name !in listOf("feature", "core")) {
//        apply(plugin = "org.jlleitschuh.gradle.ktlint")
//
//        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
//            version.set("1.2.1")
//            debug.set(true)
//            outputToConsole.set(true)
////        verbose.set(true)
//            android.set(true)
//            outputToConsole.set(true)
//            outputColorName.set("RED")
//            ignoreFailures.set(true)
//            enableExperimentalRules.set(false)
//
//            reporters {
//                reporter(ReporterType.PLAIN)
//                reporter(ReporterType.CHECKSTYLE)
//            }
//            kotlinScriptAdditionalPaths {
//                include(fileTree("scripts/"))
//            }
//            filter {
//                exclude("**/generated/**", "**/build.gradle.kts")
//                include("**/kotlin/**")
//            }
//        }
//    }
//}
