import build.*
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `java-library`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
    jacoco
    id("com.github.ben-manes.versions")
}

base.archivesName.set("touchicon-http-okhttp")
group = ProjectProperties.groupId
version = ProjectProperties.versionName

kotlin {
    jvmToolchain(11)
}

dependencies {
    api(project(":touchicon"))

    implementation(kotlin("stdlib"))
    compileOnly("com.squareup.okhttp3:okhttp:4.11.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../../docs/http-okhttp"))
    dokkaSourceSets {
        configureEach {
            moduleName.set("touchicon-http-okhttp")
        }
    }
}

tasks.named<DokkaTask>("dokkaJavadoc") {
    outputDirectory.set(File(buildDir, "docs/javadoc"))
}

tasks.create("javadocJar", Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from(File(buildDir, "docs/javadoc"))
}

tasks.create("sourcesJar", Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    archives(tasks.named<Jar>("sourcesJar"))
}

publishingSettings()
jacocoSettings()
dependencyUpdatesSettings()
