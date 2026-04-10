plugins {
    id("luau.java-library")
    id("luau.jextract")
    `maven-publish`
}

group = "me.znotchill.luau"
version = "1.0.1-patch2"
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

publishing {
    publications {
        matching { it.name == "luau"}.all {
            val pub = this as MavenPublication
            pub.groupId = project.group.toString()
            pub.artifactId = if (project.name == "native") "luau-natives" else "luau"
            pub.version = project.version.toString()

            pub.pom {
                name.set(pub.artifactId)
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