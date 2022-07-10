import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.0"

    application
}

tasks.wrapper {
    gradleVersion = "7.3.2"
}

group = "top.anagke.auto_ark"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))

    // Auto Android Framework
    implementation("top.anagke:auto-android:1.0.0")

    // Log Frameworks
    implementation("org.tinylog:tinylog-impl:2.4.1")
    implementation("org.tinylog:tinylog-api-kotlin:2.4.1")
    implementation("org.tinylog:slf4j-tinylog:2.4.1")

    // Serialization Frameworks
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")
    implementation("com.charleskorn.kaml:kaml:0.45.0")
    implementation("com.google.code.gson:gson:2.9.0")

    // Config Parsing Frameworks
    implementation("com.sksamuel.hoplite:hoplite:1.0.3")
    implementation("com.sksamuel.hoplite:hoplite-toml:2.1.5")

    // CLI Argument Parsing Frameworks
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")

    // Utils
    implementation("org.reflections:reflections:0.10.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}


distributions {
    main {
        contents {
            from(".") {
                include("base-config.toml")
            }
        }
    }
}

application {
    mainClass.set("top.anagke.MainKt")
    executableDir = ""
}
