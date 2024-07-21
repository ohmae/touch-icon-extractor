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

base.archivesName.set("touchicon-http-okhttp")
group = Projects.groupId
version = Projects.versionName
pomName = "Touch icon extractor OkHttp adapter"
pomDescription = "An adapter that replaces Touch icon extractor's Http client with OkHttp"

dependencies {
    api(projects.touchicon)

    compileOnly(libs.okhttp3)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3Mockwebserver)
}
