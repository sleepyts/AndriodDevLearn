import org.gradle.kotlin.dsl.implementation
import java.awt.SystemColor.window

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-parcelize")

    id("com.google.dagger.hilt.android") // ✅ 添加这一行
    kotlin("kapt") // ✅ 添加 kapt 支持
}

android {
    namespace = "com.example.myapplicationtest1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplicationtest1"
        minSdk = 31
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.service)
    kapt(libs.hilt.compiler)

    // Jetpack Compose 集成 Hilt
    implementation(libs.hilt.navigation.compose)
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.0") // 注意不要有空格和 - 写法错误！
    implementation ("com.google.android.exoplayer:exoplayer:2.19.0")
    implementation("androidx.compose.material:material-icons-extended:1.2.1")
    // 测试
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
