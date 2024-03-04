plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.sampah.banksampahdigital"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sampah.banksampahdigital"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
    dependenciesInfo {
        includeInApk = true
    }

}

dependencies {

    implementation (project(":core"))
    implementation (fileTree("libs").include("*jar"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}