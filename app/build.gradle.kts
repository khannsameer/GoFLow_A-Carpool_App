plugins {
    alias(libs.plugins.android.application)

    // Add the Google services Gradle plugin
    id ("com.google.gms.google-services")

}

android {
    namespace = "com.example.mad_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mad_project"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // ✅ Firebase BoM - Ensures compatibility between all Firebase libs
    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) // or latest

    // ✅ Firebase modules (no version needed)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage") // Use this instead of libs.firebase.storage

    // Your other dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}