import environment.Pj
import environment.Dep
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask
import groovy.util.Node
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    `java-library`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    maven
    `maven-publish`
    id("com.jfrog.bintray")
    jacoco
    id("com.github.ben-manes.versions")
}

base.archivesBaseName = "touchicon"
group = Pj.groupId
version = Pj.versionName

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.6"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dep.Kotlin.version}")

    testImplementation("junit:junit:4.13")
    testImplementation("com.google.truth:truth:1.0.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.8.0")
}

tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "../docs"
}

tasks.create("sourcesJar", Jar::class) {
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    archives(tasks.named<Jar>("sourcesJar"))
}

tasks.named<Upload>("uploadArchives") {
    repositories.withGroovyBuilder {
        "mavenDeployer" {
            "repository"("url" to "file:$buildDir/maven")
            "pom" {
                "project" {
                    setProperty("name", base.archivesBaseName)
                    setProperty("url", Pj.Url.site)
                    setProperty("groupId", Pj.groupId)
                    setProperty("artifactId", base.archivesBaseName)
                    setProperty("version", Pj.versionName)
                    "licenses" {
                        "license" {
                            setProperty("name", "The MIT License")
                            setProperty("url", "https://opensource.org/licenses/MIT")
                            setProperty("distribution", "repo")
                        }
                    }
                    "scm" {
                        setProperty("connection", Pj.Url.scm)
                        setProperty("url", Pj.Url.github)
                    }
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("bintray") {
            artifact("$buildDir/libs/${base.archivesBaseName}-${version}.jar")
            groupId = Pj.groupId
            artifactId = base.archivesBaseName
            version = Pj.versionName
            artifact(tasks["sourcesJar"])
            pom.withXml {
                val node = asNode()
                val licenses = node.appendNode("licenses")
                appendLicense(licenses, "The MIT License", "https://opensource.org/licenses/MIT", "repo")
                appendScm(node, Pj.Url.scm, Pj.Url.github)
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
}

fun appendLicense(parentNode: Node, name: String, url: String, distribution: String) {
    parentNode.appendNode("license").apply {
        appendNode("name", name)
        appendNode("url", url)
        appendNode("distribution", distribution)
    }
}

fun appendScm(parentNode: Node, connection: String, url: String) {
    parentNode.appendNode("scm").apply {
        appendNode("connection", connection)
        appendNode("url", url)
    }
}

fun appendDependency(parentNode: Node, groupId: String, artifactId: String, version: String, scope: String) {
    parentNode.appendNode("dependency").apply {
        appendNode("groupId", groupId)
        appendNode("artifactId", artifactId)
        appendNode("version", version)
        appendNode("scope", scope)
    }
}

bintray {
    user = project.findProperty("bintray_user") as? String ?: ""
    key = project.findProperty("bintray_key") as? String ?: ""
    setPublications("bintray")

    dryRun = true

    pkg(closureOf<PackageConfig> {
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

jacoco {
    toolVersion = "0.8.5"
}

tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    resolutionStrategy {
        rejectVersionIf {
            !isStable(candidate.version) && isStable(currentVersion)
        }
    }
}

fun isStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    return stableKeyword || regex.matches(version)
}
