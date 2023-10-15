package net.mm2d.touchicon.build

import groovy.util.Node
import org.gradle.api.*
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.plugins.signing.SigningExtension
import java.io.File
import java.net.URI

@Suppress("unused")
class MavenPublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.mavenPublishPlugin()
    }
}

private val Project.sourceSets: SourceSetContainer
    get() = (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

private fun ArtifactHandler.archives(artifactNotation: Any): PublishArtifact =
    add("archives", artifactNotation)

private val Project.publishing: PublishingExtension
    get() = (this as ExtensionAware).extensions.getByName("publishing") as PublishingExtension

private fun Project.publishing(configure: Action<PublishingExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishing", configure)

private fun Project.signing(configure: Action<SigningExtension>): Unit =
    (this as ExtensionAware).extensions.configure("signing", configure)

private val NamedDomainObjectContainer<Configuration>.api: NamedDomainObjectProvider<Configuration>
    get() = named<Configuration>("api")

private val NamedDomainObjectContainer<Configuration>.implementation: NamedDomainObjectProvider<Configuration>
    get() = named<Configuration>("implementation")

private fun Project.mavenPublishPlugin() {
    with(pluginManager) {
        apply("org.gradle.maven-publish")
        apply("org.gradle.signing")
    }
    tasks.create("javadocJar", Jar::class) {
        dependsOn("dokkaJavadoc")
        archiveClassifier.set("javadoc")
        from(File(layout.buildDirectory.asFile.get(), "docs/javadoc"))
    }
    tasks.create("sourcesJar", Jar::class) {
        dependsOn("classes")
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    artifacts {
        archives(tasks.named<Jar>("sourcesJar"))
    }
    tasks.named("publish") {
        dependsOn("assemble")
        dependsOn("javadocJar")
        dependsOn("sourcesJar")
    }
    publishing {
        publications {
            create("mavenJava", MavenPublication::class.java) {
                artifact("$buildDir/libs/${base.archivesName.get()}-${version}.jar")
                artifact(tasks.getByName("sourcesJar"))
                artifact(tasks.getByName("javadocJar"))
                groupId = Projects.groupId
                artifactId = base.archivesName.get()
                version = Projects.versionName
                pom.withXml {
                    val node = asNode()
                    node.appendNode("name", Projects.name)
                    node.appendNode("description", Projects.description)
                    node.appendNode("url", Projects.Url.site)
                    node.appendNode("licenses").appendNode("license").apply {
                        appendNode("name", "The MIT License")
                        appendNode("url", "https://opensource.org/licenses/MIT")
                        appendNode("distribution", "repo")
                    }
                    node.appendNode("developers").appendNode("developer").apply {
                        appendNode("id", Projects.developerId)
                        appendNode("name", Projects.developerName)
                    }
                    node.appendNode("scm").apply {
                        appendNode("connection", Projects.Url.scm)
                        appendNode("developerConnection", Projects.Url.scm)
                        appendNode("url", Projects.Url.github)
                    }
                    val dependencies = node.appendNode("dependencies")
                    configurations.api.get().dependencies.forEach {
                        appendDependency(
                            dependencies,
                            groupId = it.group ?: "",
                            artifactId = it.name,
                            version = it.version ?: "",
                            scope = "compile"
                        )
                    }
                    configurations.implementation.get().dependencies.forEach {
                        appendDependency(
                            dependencies,
                            groupId = it.group ?: "",
                            artifactId = it.name,
                            version = it.version ?: "",
                            scope = "runtime"
                        )
                    }
                }
            }
        }
        repositories {
            maven {
                url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2")
                credentials {
                    username = project.findProperty("sonatype_username") as? String ?: ""
                    password = project.findProperty("sonatype_password") as? String ?: ""
                }
            }
        }
    }
    signing {
        sign(publishing.publications.getByName("mavenJava"))
    }
    tasks.named("signMavenJavaPublication") {
        dependsOn("jar")
    }
}

private fun appendDependency(parentNode: Node, groupId: String, artifactId: String, version: String, scope: String) {
    parentNode.appendNode("dependency").apply {
        appendNode("groupId", groupId)
        appendNode("artifactId", artifactId)
        appendNode("version", version)
        appendNode("scope", scope)
    }
}
