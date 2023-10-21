plugins {
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.gradleVersions) apply false
    id("org.gradle.jacoco")
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
