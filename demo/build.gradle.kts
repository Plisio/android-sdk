plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "net.plisio.sdk.demo"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
        applicationId = "net.plisio.sdk.demo"
        versionCode = 1
        versionName = project.version.toString()
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("debug")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":plisio-ui-compose"))

    implementation(libs.appcompat)
    implementation(libs.activity.compose)

    implementation(libs.bundles.compose)

    implementation(libs.bundles.lifecycle)
    implementation(libs.lifecycle.viewmodel.compose)
    kapt(libs.lifecycle.compiler)

    implementation(libs.bundles.coil)

    implementation(libs.qrcode) { exclude("com.android.tools.lint") }

    coreLibraryDesugaring(libs.desugar)
}