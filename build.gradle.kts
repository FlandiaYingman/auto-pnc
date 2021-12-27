import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"

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
    implementation("ch.qos.logback:logback-core:1.3.0-alpha5")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Serialization Frameworks
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1")
    implementation("com.charleskorn.kaml:kaml:0.38.0")
    implementation("com.google.code.gson:gson:2.8.9")

    // Config Parsing Frameworks
    implementation("com.sksamuel.hoplite:hoplite:1.0.3")
    implementation("com.sksamuel.hoplite:hoplite-toml:1.4.16")

    // CLI Argument Parsing Frameworks
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")
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
