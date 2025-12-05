kotlin
plugins {
    // The version here should be a stable one, 8.2.0 or higher is fine.
    // I've kept your version, assuming it's what you intend to use.
    id("com.android.application") version "8.13.1"
    id("org.jetbrains.kotlin.android") version "1.9.0"
}

android {
    namespace = "com.example.tap2tell"
    // FIX 1: Changed from 36 to 34
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tap2tell"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // FIX 2: Removed the unnecessary buildToolsVersion line
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Note: Espresso was 3.7.0, a common stable version is 3.5.1.
    // 3.7.0 might require a newer AGP, but let's leave it for now.
    // If you get errors, change this to "3.5.1".
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
