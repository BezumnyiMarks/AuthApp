plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("com.google.devtools.ksp")
}

android {
    namespace = "com.example.authapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.authapp"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation ("com.airbnb.android:lottie:4.2.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.4.0")
    implementation ("com.squareup.moshi:moshi:1.13.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.8.0")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("androidx.room:room-runtime:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.google.dagger:hilt-android:2.38.1")
    implementation ("androidx.paging:paging-runtime:3.2.1")
    implementation ("at.blogc:expandabletextview:1.0.5")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation ("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.4.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")



    kapt ("com.google.dagger:hilt-compiler:2.38.1")
    ksp ("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    ksp ("androidx.room:room-compiler:2.5.2")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
}