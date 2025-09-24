plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // Shared domain for User, Result
    implementation(project(":shared_domain"))
    implementation(libs.kotlinx.coroutines.android)  // For Flow/suspend

    // Test deps
    testImplementation(libs.junit)
}
