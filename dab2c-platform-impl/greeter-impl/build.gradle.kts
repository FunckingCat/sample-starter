plugins {
    id("sample.kotlin-conventions")
    id("sample.publish-conventions")
}

description = "Sample starter — greeter business logic"

dependencies {
    api(project(":dab2c-platform-api:greeter-api"))
}
