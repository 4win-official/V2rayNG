// app/build.gradle.kts

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    // ۱. namespace و applicationId:
    namespace = "com.fourwin.vpn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fourwin.vpn"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // برای fetch کانفیگ
}
