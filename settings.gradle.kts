pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "touch-icon-extractor"
include(":touchicon")
include(":touchicon-http:okhttp")
