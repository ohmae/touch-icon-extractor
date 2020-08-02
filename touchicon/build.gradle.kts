import environment.Project
import environment.Dependency
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `java-library`
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

base.archivesBaseName = "touchicon"
group = Project.groupId
version = Project.Version.name

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.6"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependency.Kotlin.version}")

    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.8.0")
}

tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "../docs"
}

apply(from = "${rootDir}/common.gradle")
