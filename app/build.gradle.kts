plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    namespace = "ua.acclorite.book_story"
    compileSdk = 34

    defaultConfig {
        applicationId = "ua.acclorite.book_story"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

aboutLibraries {
    registerAndroidTasks = true
    outputFileName = "aboutlibraries.json"
    offlineMode = false
    fetchRemoteLicense = true
    fetchRemoteFunding = true
    includePlatform = true
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.SIMPLE
    prettyPrint = false
    filterVariants = arrayOf("debug", "release")
}

dependencies {

    // Default
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose BOM libraries
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.animation:animation-android")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-android")
    implementation("androidx.compose.material3:material3:latest.release")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material")

    // All dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.2-alpha")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.6")
    ksp("com.google.dagger:hilt-android-compiler:2.51")
    implementation("com.google.dagger:hilt-compiler:latest.release")
    ksp("androidx.hilt:hilt-compiler:latest.release")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:latest.release")

    // Splash API
    implementation("androidx.core:core-splashscreen:latest.release")

    // Permission Handling
    implementation("com.google.accompanist:accompanist-permissions:latest.release")

    // Pdf parser
    implementation("com.tom-roush:pdfbox-android:latest.release")

    // Epub parser
    implementation("nl.siegmann.epublib:epublib-core:latest.release") {
        exclude("org.slf4j")
        exclude("xmlpull")
    }
    implementation("org.slf4j:slf4j-android:latest.release")
    implementation("org.jsoup:jsoup:latest.release")

    // App Compat for Language Switcher
    implementation("androidx.appcompat:appcompat:latest.release")
    implementation("androidx.appcompat:appcompat-resources:latest.release")

    // Coil for loading bitmaps
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // About open source libraries
    implementation("com.mikepenz:aboutlibraries-core:latest.release")
    implementation("com.mikepenz:aboutlibraries-compose-m3:latest.release")
}