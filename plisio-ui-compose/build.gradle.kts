plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    id("maven-publish")
}

android {
    namespace = "net.plisio.sdk.ui.compose"
    compileSdk = 34
    publishing {
        multipleVariants {
            withSourcesJar()
        }
    }
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "consumer-rules.pro")
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    api(project(":plisio-sdk"))

    implementation(libs.activity.compose)

    implementation(libs.bundles.compose)

    implementation(libs.bundles.lifecycle)
    implementation(libs.lifecycle.viewmodel.compose)
    kapt(libs.lifecycle.compiler)

    implementation(libs.bundles.coil)

    implementation(libs.qrcode) { exclude("com.android.tools.lint") }

    coreLibraryDesugaring(libs.desugar)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = project.group.toString()
            artifactId = "ui-compose"
            version = project.version.toString()
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

