package net.mm2d.touchicon.build

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

class JavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugin()
    }
}

private fun Project.plugin() {
    with(pluginManager) {
        apply("org.gradle.java-library")
    }
    java {
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// DSL
private fun Project.java(action: JavaPluginExtension.() -> Unit): Unit =
    extensions.configure(action)
