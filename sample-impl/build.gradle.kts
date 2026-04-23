plugins {
    id("sample.kotlin-conventions")
    id("sample.publish-conventions")
}

description = "Sample starter — business logic implementation"

dependencies {
    api(project(":sample-api"))
}
