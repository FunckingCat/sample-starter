plugins {
    id("dab2c.kotlin-conventions")
    id("dab2c.publish-conventions")
}

description = "Sample starter — farewell business logic"

dependencies {
    api(project(":dab2c-platform-api:farewell-api"))
}
