// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("agp_version", "7.0.4")
        set("kotlin_version", "1.9.0")
        set("kotlin_coroutines", "1.3.7")
    }
}

plugins {
    id("com.android.application") version "${rootProject.extra["agp_version"]}" apply false
    id("org.jetbrains.kotlin.android") version "${rootProject.extra["kotlin_version"]}" apply false
}

extra.apply {
    set("versionCode", 1)
    set("versionNumber", "1")
    set("versionName", "1.0.0")

    set("compileSdkVersion", 33)
    set("buildToolsVersion", "33.0.2")
    set("minSdkVersion", 21)
    set("targetSdkVersion", 33)

    // androidx
    set("androidxAppcompatLibVersion", "1.3.0")

    // lib
    set("materialLibVersion", "1.1.0")
    set("gsonLibVersion", "2.9.0")
    set("okhttp3LibVersion", "4.12.0")
    set("retrofit2LibVersion", "2.9.0")
    set("zxingCoreLibVersion", "3.3.0")
}