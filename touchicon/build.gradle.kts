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
group = Projects.GROUP_ID
version = Projects.VERSION_NAME
pomName = "Touch icon extractor"
pomDescription = "This is a library to extract WebClip icon information from the website. Available in JVM and Android as this is written in pure Kotlin."

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)

    kover(projects.touchiconHttp.okhttp)
}
