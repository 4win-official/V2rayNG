android {
    namespace = "com.example.namespace" // همون رو بزارید
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.namespace" // همون مقدار باشه
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
