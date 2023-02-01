import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"

    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.kyori.blossom") version "1.2.0"
}

val name: String by project
val group: String by project
val version: String by project
val description: String by project

repositories {
    mavenCentral()

    maven(url = "https://jitpack.io")
}

val coroutines: String by project
val stdlib: String by project

val minestom: String by project
val ktoml: String by project
val minimessage: String by project

dependencies {
    implementation(kotlin("stdlib", stdlib))
    implementation("org.jetbrains.kotlinx:atomicfu:0.19.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")

    implementation("com.github.Minestom:Minestom:$minestom")
    implementation("com.akuleshov7:ktoml-core:$ktoml")
    implementation("net.kyori:adventure-text-minimessage:$minimessage")

    implementation("org.jline:jline:3.21.0")
    implementation("org.jline:jline-terminal-jansi:3.21.0")

    implementation("org.tinylog:tinylog-impl:2.5.0")
    implementation("org.tinylog:tinylog-api-kotlin:2.5.0")

    testImplementation(kotlin("test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
    archiveBaseName.set(project.name)
    archiveVersion.set("${project.version}")
    archiveClassifier.set("")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "${project.group}.atom.Main"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

blossom {
    replaceToken("@name@", name)
    replaceToken("@group@", name)
    replaceToken("@version@", version)
    replaceToken("@minecraft_version@", "")

    val commit = System.getenv("GIT_COMMIT")
    val branch = System.getenv("GIT_BRANCH")

    val file = "src/main/kotlin/it/futurecraft/atom/Atom.kt"

    replaceToken("@commit@", commit ?: "null", file)
    replaceToken("@branch@", branch ?: "null", file)
}
