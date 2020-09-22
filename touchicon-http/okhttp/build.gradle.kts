import build.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions.jvmTarget = "1.8"

dependencies {
    api(project(":touchicon"))

    implementation(kotlin("stdlib"))
    compileOnly("com.squareup.okhttp3:okhttp:3.12.10")

    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.8.1")
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../../docs/http-okhttp"))
    dokkaSourceSets {
        configureEach {
            moduleName.set("touchicon-http-okhttp")
        }
    }
}

commonSettings()
