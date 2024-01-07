package net.mm2d.touchicon.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class DocumentationDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugin()
    }
}

private fun Project.plugin() {
    with(pluginManager) {
        apply("org.jetbrains.dokka")
    }
}
