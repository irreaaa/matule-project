plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-ktor:4.0.3")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.0")

    implementation("io.ktor:ktor-server-core:3.1.3")
    implementation("io.ktor:ktor-server-netty:3.1.3")
    implementation("io.ktor:ktor-server-content-negotiation:3.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
    implementation("io.ktor:ktor-server-resources:3.1.3")
    implementation("io.ktor:ktor-server-auth:3.1.3")
    implementation("io.ktor:ktor-server-auth-jwt:3.1.3")
    implementation("io.ktor:ktor-server-config-yaml:3.1.3")

    implementation("ch.qos.logback:logback-classic:1.4.11")

    testImplementation("io.ktor:ktor-server-test-host:3.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.10")
}

