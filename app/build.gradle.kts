import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.mikepenz.aboutlibraries.plugin")
    id("androidx.room")
}

android {
    namespace = "ua.acclorite.book_story"
    compileSdk = 35

    defaultConfig {
        applicationId = "ua.acclorite.book_story"
        minSdk = 26
        targetSdk = 35
        versionCode = 10
        versionName = "1.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false

            proguardFiles("proguard-rules.pro")
        }

        create("release-debug") {
            initWith(getByName("release"))
            applicationIdSuffix = ".release.debug"
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
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

aboutLibraries {
    registerAndroidTasks = false
    prettyPrint = true
    gitHubApiToken = gradleLocalProperties(rootDir, providers)["github-key"] as? String

    filterVariants = arrayOf("debug", "release", "release-debug")
    excludeFields = arrayOf("generated", "funding", "description")
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose BOM libraries
    // Compose BOM was eliminated - it is recognized as Closed Source in AboutLibraries..
    // although it is not.
    implementation("androidx.compose.foundation:foundation:1.7.6")
    implementation("androidx.compose.animation:animation:1.7.6")
    implementation("androidx.compose.animation:animation-android:1.7.6")
    implementation("androidx.compose.foundation:foundation-layout:1.7.6")
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-graphics:1.7.6")
    implementation("androidx.compose.ui:ui-android:1.7.6")
    implementation("androidx.compose.material3:material3:1.4.0-alpha05")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("androidx.compose.material:material:1.7.6")

    // All dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.36.0")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.54")
    ksp("com.google.dagger:hilt-android-compiler:2.54")
    implementation("com.google.dagger:hilt-compiler:2.54")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // Datastore (Settings)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Permission handling
    implementation("com.google.accompanist:accompanist-permissions:0.37.0")

    // PDF parser
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")

    // EPUB parser
    implementation("org.jsoup:jsoup:1.18.3")

    // FB2 parser
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")

    // Language Switcher
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")

    // Coil for loading images
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Open source libraries
    implementation("com.mikepenz:aboutlibraries-core:11.2.3")
    implementation("com.mikepenz:aboutlibraries-compose-m3:11.2.3")

    // Drag & Drop
    implementation("sh.calvin.reorderable:reorderable:2.4.2")

    // Scrollbar
    implementation("com.github.nanihadesuka:LazyColumnScrollbar:2.2.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Markdown
    implementation("org.commonmark:commonmark:0.24.0")
}