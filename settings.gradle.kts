pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "PlisioSDK"
include(":plisio-sdk", ":plisio-ui-compose")
include(":demo")