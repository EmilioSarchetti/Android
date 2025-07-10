plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tuapp.almacenvirtual"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tuapp.almacenvirtual"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.constraintlayout)

        // MVVM - ViewModel y LiveData
        implementation(libs.androidx.lifecycle.livedata.ktx.v262)
        implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)

        // Retrofit + Gson
        implementation(libs.retrofit)
        implementation(libs.converter.gson)

        // OkHttp (para Multipart y RequestBody)
        implementation(libs.okhttp)

        // Coroutines
        implementation(libs.kotlinx.coroutines.android)

        // Glide (para mostrar imágenes)
        implementation(libs.glide)
        annotationProcessor(libs.compiler)

        // SharedPreferences
        implementation(libs.androidx.preference.ktx)

        // Test
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }

