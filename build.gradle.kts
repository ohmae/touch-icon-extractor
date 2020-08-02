buildscript {
    val versionMajor = 0
    val versionMinor = 8
    val versionPatch = 0
    val kotlinVersion by extra { "1.4.0-rc" }
    val pj by extra {
        mapOf(
            "groupId" to "net.mm2d",
            "versions" to mapOf(
                "name" to "${versionMajor}.${versionMinor}.${versionPatch}",
                "code" to versionMajor * 10000 + versionMinor * 100 + versionPatch
            ),
            "siteUrl" to "https://github.com/ohmae/touch-icon-extractor",
            "githubUrl" to "https://github.com/ohmae/touch-icon-extractor",
            "scmConnection" to "scm:git:https://github.com/ohmae/touch-icon-extractor.git"
        )
    }
    repositories {
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.10.1")

        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.28.0")
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.5"
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
