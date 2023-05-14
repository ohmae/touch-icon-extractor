buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.8.21"))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.8.10")

        classpath("com.github.ben-manes:gradle-versions-plugin:0.46.0")
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
    toolVersion = "0.8.10"
}

tasks.create("jacocoMergedReport", JacocoReport::class) {
    group = "verification"
    gradle.afterProject {
        if (rootProject != this && plugins.hasPlugin("jacoco")) {
            sourceDirectories.from += "${projectDir}/src/main/java"
            classDirectories.from.addAll(fileTree("${buildDir}/classes/kotlin/main"))
            executionData(tasks.withType<Test>())
        }
    }
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
