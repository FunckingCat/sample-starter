plugins {
    id("sample.kotlin-conventions")
}

description = "Sample starter — integration tests (not published)"

dependencies {
    testImplementation(project(":sample-starter"))
}
