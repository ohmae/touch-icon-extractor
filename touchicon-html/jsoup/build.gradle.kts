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

base.archivesBaseName = "touchicon-html-jsoup"
group = Pj.groupId
version = Pj.versionName

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.6"

dependencies {
    api(project(":touchicon"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dep.Kotlin.version}")
    implementation("org.jsoup:jsoup:1.13.1")

    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")
}

tasks.named<DokkaTask>("dokka") {
    outputFormat = "html"
    outputDirectory = "../../docs/touchicon-html"
}

setUpSourcesJar()
setUpUploadArchives()
setUpPublishing()
setUpBintray()
setUpJacoco()
setUpDependencyUpdates()
