@file:Suppress("PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_11
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "de.hglabor"
version = "1.0.0"

/*
 * PLUGINS
 */

plugins {

    kotlin("jvm") version "1.4.21"

    id("com.github.johnrengelman.shadow") version "6.1.0"

    kotlin("plugin.serialization") version "1.4.21"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenLocal()
    jcenter()
    maven("https://papermc.io/repo/repository/maven-public/")
    // FAWE
    maven("https://mvn.intellectualsites.com/content/repositories/releases/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("reflect"))

    // SPIGOT
    //compileOnly("org.spigotmc", "spigot", "1.16.5-R0.1-SNAPSHOT")

    // PAPER
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")


    // KSPIGOT
    implementation("net.axay", "KSpigot", "v1.16.5_R23")

    // KMONGO and MONGODB
    implementation("org.litote.kmongo", "kmongo-core", "4.2.3")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.3")

    implementation("com.intellectualsites.fawe:FAWE-Bukkit:1.16-583")
}

/*
 * BUILD
 */

// JVM VERSION

java.sourceCompatibility = JVM_VERSION
java.targetCompatibility = JVM_VERSION

tasks.withType<KotlinCompile> {
    configureJvmVersion()
}

// SHADOW

tasks {
    shadowJar {
        simpleRelocate("net.axay.kspigot")
    }
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString
    get() = majorVersion.let {
        val version = it.toInt()
        if (version <= 10) "1.$it" else it
    }

fun KotlinCompile.configureJvmVersion() {
    kotlinOptions.jvmTarget = JVM_VERSION_STRING
}

fun ShadowJar.simpleRelocate(pattern: String) {
    relocate(pattern, "${project.group}.${project.name.toLowerCase()}.shadow.$pattern")
}