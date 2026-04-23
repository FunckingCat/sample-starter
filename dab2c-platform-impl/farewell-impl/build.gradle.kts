plugins {
    id("sample.kotlin-conventions")
    id("sample.publish-conventions")
}

description = "Sample starter — farewell business logic"

dependencies {
    api(project(":dab2c-platform-api:farewell-api"))
}
