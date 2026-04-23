plugins {
    `java-library`
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

dependencies {
    "implementation"(platform(libs.findLibrary("spring-boot-dependencies").get()))
    "testImplementation"(platform(libs.findLibrary("spring-boot-dependencies").get()))
    "testImplementation"(libs.findLibrary("spring-boot-starter-test").get())
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}
