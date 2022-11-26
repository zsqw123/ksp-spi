plugins {
    kotlin("jvm")
}

group = "com.zsu"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":spi-api"))
    implementation(project(":demo-api"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}