plugins {
    id("sample.kotlin-conventions")
    id("sample.publish-conventions")
}

description = "Sample starter — Spring Boot auto-configuration"

dependencies {
    api(project(":dab2c-platform-api:greeter-api"))
    api(project(":dab2c-platform-api:farewell-api"))
    implementation(project(":dab2c-platform-impl:greeter-impl"))
    implementation(project(":dab2c-platform-impl:farewell-impl"))
    implementation(libs.spring.boot.autoconfigure)
    annotationProcessor(libs.spring.boot.configuration.processor)
}
