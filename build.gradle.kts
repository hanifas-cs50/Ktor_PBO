plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "1.9.0" // ✅ Add this (match your Kotlin version)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)

    // Web handling
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.1") // kotlinx.html (web templating)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") // kotlinx.html serialization
    implementation("io.ktor:ktor-http:2.3.7") // for HttpStatusCode
    implementation("io.ktor:ktor-server-sessions:2.3.7") // for session management
    implementation("io.ktor:ktor-server-html-builder:2.3.7") // for receiveParameters (HTML forms)
    implementation("io.ktor:ktor-server-status-pages:2.3.7") // optional but helpful for error responses

    // Password hashing
    implementation("org.mindrot:jbcrypt:0.4")

    // Exposed ORM + SQLite
    implementation("org.jetbrains.exposed:exposed-core:0.48.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.48.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.48.0")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
