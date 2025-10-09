plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tickytackytoes"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tickytackytoes"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.01-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        create("alpha") {
            initWith(getByName("debug")) // inherit from debug (so it’s debuggable + easy)
            isDebuggable = true
            applicationIdSuffix = ".alpha"
            versionNameSuffix = "-ALPHA"
        }
        create("beta") {
            initWith(getByName("debug")) // inherit from debug (so it’s debuggable + easy)
            isDebuggable = true
            applicationIdSuffix = ".alpha"
            versionNameSuffix = "-ALPHA"
        }
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
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src\\main\\java", "src\\main\\java\\helpers")
            }
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

annotation class initWith(val value: String)
