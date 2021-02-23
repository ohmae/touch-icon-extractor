buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")

        classpath("com.github.ben-manes:gradle-versions-plugin:0.36.0")
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.6"
}

tasks.create("jacocoMerge", JacocoMerge::class) {
    group = "verification"
    gradle.afterProject {
        if (rootProject != this && plugins.hasPlugin("jacoco")) {
            executionData("${buildDir}/jacoco/test.exec")
        }
    }
}

tasks.create("jacocoMergedReport", JacocoReport::class) {
    group = "verification"
    dependsOn("jacocoMerge")
    gradle.afterProject {
        if (rootProject != this && plugins.hasPlugin("jacoco")) {
            sourceDirectories.from += "${projectDir}/src/main/java"
            classDirectories.from.addAll(fileTree("${buildDir}/classes/kotlin/main"))
        }
    }
    executionData(tasks.named<JacocoMerge>("jacocoMerge").get().destinationFile)
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}
