// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    dependencies {
        // سایر dependencies
        classpath("com.google.code.gson:gson:2.9.0")
        classpath("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
