// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.android.library) apply false
    // alias(libs.plugins.ksp) apply false  // Uncomment if needed globally
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}