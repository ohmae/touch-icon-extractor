import net.mm2d.build.Projects
import net.mm2d.build.pomDescription
import net.mm2d.build.pomName

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
pomName = "Touch icon extractor"
pomDescription = "Touch icon extractor"

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)

    kover(projects.touchiconHttp.okhttp)
}
