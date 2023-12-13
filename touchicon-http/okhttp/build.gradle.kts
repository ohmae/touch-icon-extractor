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

base.archivesName.set("touchicon-http-okhttp")
group = Projects.groupId
version = Projects.versionName

dependencies {
    api(project(":touchicon"))

    compileOnly(libs.okhttp3)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)
}

tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(File(projectDir, "../../docs/http-okhttp"))
    dokkaSourceSets {
        configureEach {
            moduleName.set("touchicon-http-okhttp")
        }
    }
}

tasks.named<DokkaTask>("dokkaJavadoc") {
    outputDirectory.set(File(layout.buildDirectory.asFile.get(), "docs/javadoc"))
}
