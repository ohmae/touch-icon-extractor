package net.mm2d.touchicon.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class DocumentationDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.documentationDokkaPlugin()
    }
}

private fun Project.documentationDokkaPlugin() {
    with(pluginManager) {
        apply("org.jetbrains.dokka")
    }
}
