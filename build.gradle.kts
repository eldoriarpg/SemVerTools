import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    java
    `maven-publish`
    id("org.cadixdev.licenser") version "0.6.1"
    id("net.ltgt.errorprone") version "0.6"
    id("me.champeau.jmh") version "0.6.5"
}

group = "de.eldoria"
version = "1.0.0-alpha"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains", "annotations", "22.0.0")

    annotationProcessor("com.uber.nullaway", "nullaway", "0.9.2")
    errorprone("com.google.errorprone", "error_prone_core", "2.9.0")
    errorproneJavac("com.google.errorprone", "javac", "9+181-r4173-1")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.7.2")
    testImplementation("org.junit.jupiter", "junit-jupiter-params", "5.7.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.7.2")
}

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_11;
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

}

publishing {
    val publishData = PublishData(project)

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.group as String?
            artifactId = project.name.toLowerCase()
            version = publishData.getVersion()
        }
    }

    repositories {
        maven {
            name = "EldoNexus"
            url = uri(publishData.getRepository())

            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }
        }
    }
}

configure<LicenseExtension> {
    header(project.file("HEADER.txt"))
}

class PublishData(private val project: Project) {
    var type: Type = getReleaseType()
    var hashLength: Int = 7

    private fun getReleaseType(): Type {
        val branch = getCheckedOutBranch()
        return when {
            branch.contentEquals("master") -> Type.RELEASE
            branch.startsWith("dev") -> Type.DEV
            else -> Type.SNAPSHOT
        }
    }

    private fun getCheckedOutGitCommitHash(): String = System.getenv("GITHUB_SHA")?.substring(0, hashLength) ?: "local"

    private fun getCheckedOutBranch(): String = System.getenv("GITHUB_REF")?.replace("refs/heads/", "") ?: "local"

    fun getVersion(): String = getVersion(false)

    fun getVersion(appendCommit: Boolean): String =
        type.append(getVersionString(), appendCommit, getCheckedOutGitCommitHash())

    private fun getVersionString(): String = (project.version as String).replace("-SNAPSHOT", "").replace("-DEV", "")

    fun getRepository(): String = type.repo

    enum class Type(private val append: String, val repo: String, private val addCommit: Boolean) {
        RELEASE("", "https://eldonexus.de/repository/maven-releases/", false),
        DEV("-DEV", "https://eldonexus.de/repository/maven-dev/", true),
        SNAPSHOT("-SNAPSHOT", "https://eldonexus.de/repository/maven-snapshots/", true);

        fun append(name: String, appendCommit: Boolean, commitHash: String): String =
            name.plus(append).plus(if (appendCommit && addCommit) "-".plus(commitHash) else "")
    }
}
