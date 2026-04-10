plugins {
    id("luau.java-library")
    id("luau.jextract")
    `maven-publish`
}

group = "me.znotchill.luau"
version = "1.0.1-patch"
description = "Java bindings for Luau"

dependencies {
    testImplementation(project(":native"))
}

sourceSets {
    main {
        java.srcDir(file("src/main/java"))
        java.srcDir(file("src/generated/java"))
    }
}

tasks.withType<Jar> {
    archiveBaseName = "luau"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

publishing.publications.create<MavenPublication>("luau") {
    groupId = project.group.toString()
    artifactId = "luau"
    version = project.version.toString()

    from(project.components["java"])

    pom {
        name.set(artifactId)

        configureMavenPom(this)
    }
}

publishing {
    publications {
        named<MavenPublication>("luau") {
            groupId = project.group.toString()
            artifactId = if (project.name == "native") "luau-natives" else "luau"
            version = project.version.toString()

            pom {
                name.set(artifactId)
                configureMavenPom(this)
            }
        }
    }

    repositories {
        maven {
            name = "znotchill"
            url = uri("https://repo.znotchill.me/repository/maven-releases/")
            credentials {
                username = findProperty("zRepoUsername") as String? ?: System.getenv("MAVEN_USER")
                password = findProperty("zRepoPassword") as String? ?: System.getenv("MAVEN_PASS")
            }
        }
        mavenLocal()
    }
}