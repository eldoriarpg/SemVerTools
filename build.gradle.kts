import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    `maven-publish`
    id("org.cadixdev.licenser") version "0.6.1"
    id("net.ltgt.errorprone") version "3.0.1"
    id("me.champeau.jmh") version "0.6.8"
    id("de.chojo.publishdata") version "1.0.1"
}

group = "de.eldoria"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains", "annotations", "24.0.0")

    annotationProcessor("com.uber.nullaway", "nullaway", "0.10.9")
    errorprone("com.google.errorprone", "error_prone_core", "2.18.0")
    errorproneJavac("com.google.errorprone", "javac", "9+181-r4173-1")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.11.3")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.11.3")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.11.3")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = "UTF-8"
        if (!name.toLowerCase().contains("test")
            && !name.toLowerCase().contains("jmh")
        ) {
            options.errorprone.apply {
                check("NullAway", CheckSeverity.ERROR)
                option("NullAway:AnnotatedPackages", "de.eldoria.semvertools")
            }
        }
    }

    compileTestJava{
        options.encoding = "UTF-8"
    }

    javadoc {
        (options as StandardJavadocDocletOptions).tags?.add("apiNote:a:API Note")
    }
}

license {
    header(project.file("HEADER.txt"))
}

publishData {
    useEldoNexusRepos()
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            url = uri(publishData.getRepository())
        }
    }
}
