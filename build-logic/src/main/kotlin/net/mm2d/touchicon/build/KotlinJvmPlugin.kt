package net.mm2d.touchicon.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinJvmPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.kotlinJvmPlugin()
    }
}

private fun Project.kotlin(action: KotlinJvmProjectExtension.() -> Unit): Unit =
    extensions.configure(action)

private fun Project.kotlinJvmPlugin() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.jvm")
    }
    kotlin {
        jvmToolchain(11)
    }
    dependencies {
        implementation(libs.library("kotlinStdlib"))
    }
}
