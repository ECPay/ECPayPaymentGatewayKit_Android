plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
    buildToolsVersion = rootProject.extra["buildToolsVersion"] as String

    flavorDimensions("default")

    buildFeatures {
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
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String
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

    packagingOptions {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["kotlin_coroutines"]}")

    implementation("androidx.appcompat:appcompat:${rootProject.extra["androidxAppcompatLibVersion"]}")

    implementation("com.google.android.material:material:${rootProject.extra["materialLibVersion"]}")
    implementation("com.google.code.gson:gson:${rootProject.extra["gsonLibVersion"]}")
    implementation("com.google.zxing:core:${rootProject.extra["zxingCoreLibVersion"]}")

    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra["okhttp3LibVersion"]}")
    implementation("com.squareup.okhttp3:logging-interceptor:${rootProject.extra["okhttp3LibVersion"]}")
    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit2LibVersion"]}")
    implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["retrofit2LibVersion"]}")

    implementation("tw.com.ecpay:ECPayPaymentGatewayKit:1.5.0")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}