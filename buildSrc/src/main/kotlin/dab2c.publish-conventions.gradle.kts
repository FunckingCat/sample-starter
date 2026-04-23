plugins {
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set(project.description ?: project.name)
            }
        }
    }
    repositories {
        mavenLocal()
    }
}
