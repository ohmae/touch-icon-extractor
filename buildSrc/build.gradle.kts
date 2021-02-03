plugins {
    kotlin("jvm") version "1.4.21"
    `kotlin-dsl`
}

repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.36.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
