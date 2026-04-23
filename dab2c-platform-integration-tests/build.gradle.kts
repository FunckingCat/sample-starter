plugins {
    id("dab2c.kotlin-conventions")
}

description = "Sample starter — integration tests (not published)"

dependencies {
    testImplementation(project(":dab2c-platform-starter"))
}
