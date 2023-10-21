// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.4")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:3.4.0")
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.android.library") version "8.1.0" apply false
}