import net.mm2d.touchicon.build.Projects
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.gradle.java-library")
    id("build.logic.kotlinJvm")
    id("build.logic.kover")
    id("build.logic.documentationDokka")
    id("build.logic.mavenPublish")
    id("build.logic.gradleVersions")
}

base.archivesName.set("touchicon")
group = Projects.groupId
version = Projects.versionName

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)

    kover(project(":touchicon-http:okhttp"))
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../docs/touchicon"))
}

tasks.named<DokkaTask>("dokkaJavadoc") {
    outputDirectory.set(File(layout.buildDirectory.asFile.get(), "docs/javadoc"))
}
