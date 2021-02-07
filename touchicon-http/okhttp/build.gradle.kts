import build.*
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `java-library`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    maven
    `maven-publish`
    id("com.jfrog.bintray")
    jacoco
    id("com.github.ben-manes.versions")
}

base.archivesBaseName = "touchicon-http-okhttp"
group = ProjectProperties.groupId
version = ProjectProperties.versionName

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":touchicon"))

    implementation(kotlin("stdlib"))
    compileOnly("com.squareup.okhttp3:okhttp:3.12.10")

    testImplementation("junit:junit:4.13.1")
    testImplementation("com.google.truth:truth:1.1.2")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../../docs/http-okhttp"))
    dokkaSourceSets {
        configureEach {
            moduleName.set("touchicon-http-okhttp")
        }
    }
}

tasks.create("sourcesJar", Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    archives(tasks.named<Jar>("sourcesJar"))
}

uploadArchivesSettings()
publishingSettings()
bintraySettings()
jacocoSettings()
dependencyUpdatesSettings()
