// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// توجه: مخازن نباید اینجا تعریف بشن چون settings.gradle.kts مسئول اونهاست

buildscript {
    dependencies {
        // افزودن Gson و Coroutines به صورت dependency مشترک
        // توجه: معمولاً اینها باید داخل build.gradle.kts مربوط به ماژول app تعریف بشن، نه اینجا
    }
}
