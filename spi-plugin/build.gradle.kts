plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.zsu"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":spi-api"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.21-1.0.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    val poetVersion = "1.12.0"
    implementation("com.squareup:kotlinpoet:$poetVersion")
    implementation("com.squareup:kotlinpoet-ksp:$poetVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}