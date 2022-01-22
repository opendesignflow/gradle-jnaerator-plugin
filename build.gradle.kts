plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    id("java-gradle-plugin")


    // Publish
    id("maven-publish")
    id("com.gradle.plugin-publish") version ("0.18.0")
}


// Project version
var lib_version by extra("1.0.1-SNAPSHOT")
var branch by extra { System.getenv("BRANCH_NAME") }
if (System.getenv().getOrDefault("BRANCH_NAME", "dev").contains("release")) {
    lib_version = lib_version.replace("-SNAPSHOT", "")
}

group = "org.odfi.anarres"
version = lib_version

pluginBundle {
    website = "https://github.com/opendesignflow/gradle-jnaerator-plugin"
    vcsUrl = "https://github.com/opendesignflow/gradle-jnaerator-plugin"
    tags = listOf("gradle", "plugin", "jnaerator", "jna", "bridj")
    //tags = ["gradle", "plugin", "jnaerator", "jna", "bridj"]

    //tags = ["xml", "scala", "json", "jsonb", "generator", "marshall", "unmarshall"]
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
    // withJavadocJar()
    withSourcesJar()
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

dependencies {

    implementation("com.google.code.findbugs:annotations:3.0.1")
    implementation("com.nativelibs4java:jnaerator:0.12")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.11.0")


}

gradlePlugin {
    // Define the plugin
    plugins {
        this.create("jnaeratorPlugin").apply {
            id = "org.odfi.anarres.jnaerator"
            implementationClass = "org.anarres.gradle.plugin.jnaerator.JNAeratorPlugin"
            displayName = "Anarres JNAERATOR Plugin (ODFI build)"
            description = "A Gradle plugin for the JNAerator binding generator."
        }


    }
}

publishing {
    publications {

        create<MavenPublication>("maven") {
            artifactId = "jnaerator-gradle-plugin"
            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("Anarres JNAERATOR Plugin (ODFI update)")
                description.set("A Gradle plugin for the JNAerator binding generator.")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {


                    developer {
                        id.set("shevek")
                        name.set("shevek")
                        email.set("github@anarres.org")
                    }

                    developer {
                        id.set("richnou")
                        name.set("Richnou")
                        email.set("leys.richard@gmail.com")
                    }
                }
            }
        }

    }
    repositories {
        maven {

            // change URLs to point to your repos, e.g. http://my.org/repo
            var releasesRepoUrl = uri("https://repo.opendesignflow.org/maven/repository/internal/")
            var snapshotsRepoUrl = uri("https://repo.opendesignflow.org/maven/repository/snapshots")

            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            // Credentials
            //-------------
            credentials {
                username = System.getenv("PUBLISH_USERNAME")
                password = System.getenv("PUBLISH_PASSWORD")
            }
        }
    }
}

repositories {

    mavenLocal()
    mavenCentral()
    maven {
        name = "Sonatype Nexus Snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "ODFI Releases"
        url = uri("https://repo.opendesignflow.org/maven/repository/internal/")
    }
    maven {
        name = "ODFI Snapshots"
        url = uri("https://repo.opendesignflow.org/maven/repository/snapshots/")
    }

    google()
}
