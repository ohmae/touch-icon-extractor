package net.mm2d.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class KoverPlugin  : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugin()
    }
}

private fun Project.plugin() {
    with(pluginManager) {
        apply("org.jetbrains.kotlinx.kover")
    }
}
