package build

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

fun Project.setUpDependencyUpdates() {
    tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
        resolutionStrategy {
            rejectVersionIf {
                !isStable(candidate.version) && isStable(currentVersion)
            }
        }
    }
}

private fun isStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    return stableKeyword || regex.matches(version)
}
