import nl.littlerobots.vcu.plugin.resolver.VersionSelectors
import nl.littlerobots.vcu.plugin.versionCatalogUpdate

plugins {
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokkaJavadoc) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.vanniktechMavenPublish) apply false
    alias(libs.plugins.versionCatalogUpdate)
}

dependencies {
    val dokkaPluginId = libs.plugins.build.logic.documentationDokka.get().pluginId
    subprojects {
        plugins.withId(dokkaPluginId) {
            dokka(this@subprojects)
        }
    }
}

dokka {
    basePublicationsDirectory.set(File(projectDir, "docs/dokka"))
}

versionCatalogUpdate {
    versionSelector(VersionSelectors.STABLE)
}

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

tasks.register<JavaExec>("ktlint") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
    isIgnoreExitValue = true
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
    isIgnoreExitValue = true
}
