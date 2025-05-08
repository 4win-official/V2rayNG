plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.namespace" // جایگزین کن با نام‌دلخواه مثل com.fourwin.vpn
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.namespace" // همون مقدار namespace
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
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // برای دریافت JSON از گیت‌هاب
}
