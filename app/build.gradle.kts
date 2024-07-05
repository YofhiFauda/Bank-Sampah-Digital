plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

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

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")

    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.annotation:annotation:1.8.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.github.yalantis:ucrop:2.2.7")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //DataStore
    implementation ("androidx.datastore:datastore-core:1.1.1")
    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-core:1.1.1")

    implementation("androidx.compose.ui:ui-graphics-android:1.6.7")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.0")
    implementation("androidx.activity:activity-ktx:1.9.0")

    //CameraX
    implementation ("com.google.guava:guava:32.0.1-android")
    implementation ("androidx.camera:camera-camera2:1.4.0-beta01")
    implementation("androidx.camera:camera-view:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.activity:activity:1.9.0")

    // Unit testing dependencies
    debugImplementation("androidx.fragment:fragment-testing:1.7.1")

    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.test:runner:1.5.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.mockito:mockito-inline:4.6.1") // Tambahkan ini
    testImplementation("androidx.test:core:1.5.0") // Tambahkan ini

    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("org.mockito:mockito-android:4.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
}