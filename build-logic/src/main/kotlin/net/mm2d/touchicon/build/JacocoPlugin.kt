package net.mm2d.touchicon.build

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.jacocoPlugin()
    }
}

private fun Project.jacocoPlugin() {
    with(pluginManager) {
        apply("org.gradle.jacoco")
    }
    jacoco {
        toolVersion = "0.8.10"
    }
    tasks.named("jacocoTestReport", JacocoReport::class.java) {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

// DSL

private fun Project.jacoco(configure: Action<JacocoPluginExtension>): Unit =
    (this as ExtensionAware).extensions.configure("jacoco", configure)
