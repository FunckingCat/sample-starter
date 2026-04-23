pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "dab2c-platform-parent"

include("dab2c-platform-api:greeter-api")
include("dab2c-platform-api:farewell-api")
include("dab2c-platform-impl:greeter-impl")
include("dab2c-platform-impl:farewell-impl")
include("dab2c-platform-starter")
include("dab2c-platform-integration-tests")
