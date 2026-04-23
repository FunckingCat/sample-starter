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

rootProject.name = "sample-starter-parent"

include("sample-api")
include("sample-impl")
include("sample-starter")
include("sample-integration-tests")
