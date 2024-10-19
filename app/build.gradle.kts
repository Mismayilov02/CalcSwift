plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id ("kotlin-kapt")
}

android {
    namespace = "com.imcalculator.calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.imcalculator.calculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "2.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.google.android.flexbox:flexbox:3.0.0")

//    // Room components
//    implementation( "androidx.room:room-runtime:2.6.1")
//    kapt ("androidx.room:room-compiler:2.6.1")
//    implementation( "androidx.room:room-ktx:2.6.1")
//    androidTestImplementation ("androidx.room:room-testing:2.6.1")
//
//
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // SharedPreferences encryption
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

}