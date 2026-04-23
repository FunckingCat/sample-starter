plugins {
    id("sample.kotlin-conventions")
    id("sample.publish-conventions")
}

description = "Sample starter — Spring Boot auto-configuration"

dependencies {
    api(project(":sample-api"))
    implementation(project(":sample-impl"))
    implementation(libs.spring.boot.autoconfigure)
    annotationProcessor(libs.spring.boot.configuration.processor)
}
