plugins {
    id("com.android.application") version "8.13.1"
    id("org.jetbrains.kotlin.android") version "1.9.0"

    // ADDED: Google Services plugin
    id("com.google.gms.google-services") version "4.4.1"

    // ADDED: Firebase Crashlytics plugin
    id("com.google.firebase.crashlytics") version "2.9.9"
}

android {
    namespace = "com.example.tap2tell"
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    // --- ADDED FIREBASE DEPENDENCIES ---
    // Import the Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Dependency for Firebase Crashlytics
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Dependency for Firebase Analytics (recommended for Crashlytics)
    implementation("com.google.firebase:firebase-analytics-ktx")
}
