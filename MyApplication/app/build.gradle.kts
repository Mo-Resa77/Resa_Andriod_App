plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 35
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Core AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.6.1") // Latest stable version
    implementation("com.google.android.material:material:1.11.0") // Ensure compatibility
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0") // Stable version

    // Testing libraries
    testImplementation("junit:junit:4.13.2") // Latest JUnit for unit testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Latest JUnit extensions
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Stable Espresso version

    // Lottie animation library
    implementation("com.airbnb.android:lottie:6.0.0") // Update to the latest version
}
