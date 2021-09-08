import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    java
    id("org.cadixdev.licenser") version "0.6.1"
    id("net.ltgt.errorprone") version "0.6"
    id("me.champeau.jmh") version "0.6.5"
}

group = "de.eldoria.semvertools"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.jetbrains:annotations:22.0.0")

    annotationProcessor("com.uber.nullaway:nullaway:0.9.2")
    errorprone("com.google.errorprone:error_prone_core:2.9.0")
    errorproneJavac("com.google.errorprone:javac:9+181-r4173-1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<JavaCompile>("compileJava") {
    if (!name.toLowerCase().contains("test")
            && !name.toLowerCase().contains("jmh")) {
        options.errorprone.apply {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", "de.eldoria.semvertools")
        }
    }
}

configure<LicenseExtension> {
    header(project.file("HEADER.txt"))
}