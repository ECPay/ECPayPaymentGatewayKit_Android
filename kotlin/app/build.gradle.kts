plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "tw.com.ecpay.paymentgatewaykit.example"
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    buildToolsVersion = libs.versions.buildToolsVersion.get()

    flavorDimensions.addAll(mutableListOf("default"))

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    signingConfigs {
        create("release") {
            enableV3Signing = true
            storeFile = file("ecpay_debug.keystore")
            storePassword = "ecpay123456"
            keyAlias = "ecpaydebugkey"
            keyPassword = "ecpay123456"
        }
        getByName("debug") {
            enableV3Signing = true
            storeFile = file("ecpay_debug.keystore")
            storePassword = "ecpay123456"
            keyAlias = "ecpaydebugkey"
            keyPassword = "ecpay123456"
        }
    }

    defaultConfig {
        applicationId = "tw.com.ecpay.paymentgatewaykit.gateway.example"
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/*.version",
                "META-INF/*.kotlin_module",
                "kotlin/**",
                "okhttp3/**",
                "/*.properties"
            )
        }
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
    // Kotlin
    implementation(libs.jetbrains.kotlinx.coroutines.android)
    // Jetpack AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    // Google Lib
    implementation(libs.google.material)
    implementation(libs.google.gson)
    implementation(libs.zxing.core)
    // Lib
    implementation(libs.squareup.okhttp3.okhttp)
    implementation(libs.squareup.okhttp3.logging)
    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter.gson)
    // Sdk
    implementation("tw.com.ecpay:ECPayPaymentGatewayKit:1.7.0")

    testImplementation(libs.junit4)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}