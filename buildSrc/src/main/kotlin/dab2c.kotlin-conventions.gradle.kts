import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("dab2c.java-conventions")
    kotlin("jvm")
    kotlin("plugin.spring")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

dependencies {
    "implementation"(kotlin("reflect"))
}
