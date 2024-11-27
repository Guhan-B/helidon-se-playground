plugins {
    id("java")
    id("application")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.playground.helidon.Main"
}

version = "0.0.1-SNAPSHOT"
group = "com.playground"
description = "Helidon SE - Helidon Configuration API"

ext {
    set("helidonVersion", "4.1.4")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:${project.ext["helidonVersion"]}"))

    implementation("io.helidon.config:helidon-config-yaml")
}