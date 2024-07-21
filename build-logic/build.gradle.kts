plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.bundles.plugins)
}

gradlePlugin {
    plugins {
        register("javaLibrary") {
            id = "build.logic.javaLibrary"
            implementationClass = "net.mm2d.build.JavaLibraryPlugin"
        }
        register("kotlinJvm") {
            id = "build.logic.kotlinJvm"
            implementationClass = "net.mm2d.build.KotlinJvmPlugin"
        }
        register("kover") {
            id = "build.logic.kover"
            implementationClass = "net.mm2d.build.KoverPlugin"
        }
        register("mavenPublish") {
            id = "build.logic.mavenPublish"
            implementationClass = "net.mm2d.build.MavenPublishPlugin"
        }
        register("documentationDokka") {
            id = "build.logic.documentationDokka"
            implementationClass = "net.mm2d.build.DocumentationDokkaPlugin"
        }
        register("gradleVersions") {
            id = "build.logic.gradleVersions"
            implementationClass = "net.mm2d.build.GradleVersionsPlugin"
        }
        register("dependencyGuard") {
            id = "build.logic.dependencyGuard"
            implementationClass = "net.mm2d.build.DependencyGuardPlugin"
        }
    }
}
