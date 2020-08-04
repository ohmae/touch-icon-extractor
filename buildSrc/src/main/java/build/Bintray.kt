package build

import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.getPluginByName

private fun Project.bintray(configure: BintrayExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("bintray", configure)

private val Project.base: BasePluginConvention
    get() = ((this as? Project)?.convention ?: (this as HasConvention).convention).getPluginByName("base")

fun Project.setUpBintray() {
    bintray {
        user = project.findProperty("bintray_user") as? String ?: ""
        key = project.findProperty("bintray_key") as? String ?: ""
        setPublications("bintray")

        dryRun = true

        pkg(closureOf<BintrayExtension.PackageConfig> {
            repo = "maven"
            name = Pj.groupId + "." + base.archivesBaseName
            setLicenses("MIT")
            websiteUrl = Pj.Url.site
            vcsUrl = Pj.Url.github + ".git"
            issueTrackerUrl = Pj.Url.github + "/issues"
            publicDownloadNumbers = true
            version = VersionConfig().apply {
                name = Pj.versionName
            }
        })
    }

    tasks.named("bintrayUpload") {
        dependsOn("assemble")
    }
}
