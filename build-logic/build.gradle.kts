import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.bundles.plugins)
}

gradlePlugin {
    plugins {
        register("kotlinJvm") {
            id = "build.logic.kotlinJvm"
            implementationClass = "net.mm2d.touchicon.build.KotlinJvmPlugin"
        }
        register("jacoco") {
            id = "build.logic.jacoco"
            implementationClass = "net.mm2d.touchicon.build.JacocoPlugin"
        }
        register("mavenPublish") {
            id = "build.logic.mavenPublish"
            implementationClass = "net.mm2d.touchicon.build.MavenPublishPlugin"
        }
        register("documentationDokka") {
            id = "build.logic.documentationDokka"
            implementationClass = "net.mm2d.touchicon.build.DocumentationDokkaPlugin"
        }
        register("gradleVersions") {
            id = "build.logic.gradleVersions"
            implementationClass = "net.mm2d.touchicon.build.GradleVersionsPlugin"
        }
    }
}
