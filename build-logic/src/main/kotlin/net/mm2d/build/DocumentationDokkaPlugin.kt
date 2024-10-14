package net.mm2d.build

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.dokka.gradle.DokkaExtension

class DocumentationDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugin()
    }
}

private fun Project.plugin() {
    with(pluginManager) {
        apply("org.jetbrains.dokka")
        apply("org.jetbrains.dokka-javadoc")
    }
    dokka {
        moduleName.set(base.archivesName.get())
    }
}

// DSL
private fun Project.dokka(configure: Action<DokkaExtension>): Unit =
    (this as ExtensionAware).extensions.configure("dokka", configure)
