plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.febin.di"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // All features for bindings
    implementation(project(":shared_domain"))
    implementation(project(":feature:authentication:domain"))
    implementation(project(":feature:authentication:data"))
    implementation(project(":feature:authentication:presentation"))
    implementation(project(":feature:userdashboard:domain"))
    implementation(project(":feature:userdashboard:data"))
    implementation(project(":feature:userdashboard:presentation"))
    implementation(project(":feature:admindashboard:domain"))
    implementation(project(":feature:admindashboard:data"))
    implementation(project(":feature:admindashboard:presentation"))
    implementation(project(":app"))
    


    // Room for DB (if global)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}