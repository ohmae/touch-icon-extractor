package net.mm2d.touchicon.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class KoverPlugin  : Plugin<Project> {
    override fun apply(target: Project) {
        target.koverPlugin()
    }
}

private fun Project.koverPlugin() {
    with(pluginManager) {
        apply("org.jetbrains.kotlinx.kover")
    }
}
