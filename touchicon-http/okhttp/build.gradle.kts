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
group = Properties.groupId
version = Properties.versionName

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.6"

dependencies {
    api(project(":touchicon"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Libraries.Kotlin.version}")
    compileOnly("com.squareup.okhttp3:okhttp:3.12.10")

    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.8.0")
}

tasks.named<DokkaTask>("dokka") {
    outputFormat = "html"
    outputDirectory = "../../docs/touchicon-http"
}

commonSettings()
