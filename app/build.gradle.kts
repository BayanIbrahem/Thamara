@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinxSerializationPlugin)
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.gms)
}

android {
    namespace = "com.dev_bayan_ibrahim.flashcards"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev_bayan_ibrahim.flashcards"
        minSdk = 21
        targetSdk = 34
        versionCode = 16
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"

        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material)

    // general
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serilalization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.windowsizeclass)

    // room
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.compose)

    // data store
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // coil
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt)

    // ktor
    implementation(libs.ktor.core)
    implementation(libs.ktor.engine.android)
    implementation(libs.ktor.logging)
//    implementation(libs.ktor.auth)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.guava)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firabse.installations)
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)

//    implementation(libs.yChart)

    // desugar
    coreLibraryDesugaring(libs.android.desugar.jdk.lib)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}