import net.mm2d.touchicon.build.Projects
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.build.logic.javaLibrary)
    alias(libs.plugins.build.logic.kotlinJvm)
    alias(libs.plugins.build.logic.kover)
    alias(libs.plugins.build.logic.documentationDokka)
    alias(libs.plugins.build.logic.mavenPublish)
    alias(libs.plugins.build.logic.gradleVersions)
    alias(libs.plugins.build.logic.dependencyGuard)
}

base.archivesName.set("touchicon")
group = Projects.groupId
version = Projects.versionName

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)

    kover(projects.touchiconHttp.okhttp)
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../docs/touchicon"))
}

tasks.named<DokkaTask>("dokkaJavadoc") {
    outputDirectory.set(File(layout.buildDirectory.asFile.get(), "docs/javadoc"))
}
