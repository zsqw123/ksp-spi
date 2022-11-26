plugins {
    kotlin("jvm")
    application
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
}

group = "com.zsu"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":spi-api"))
    implementation(project(":demo-api"))
    implementation(project(":demo-child"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application.mainClass.set("com.zsu.demo.main.MainKt")

ksp {
    arg("spi-loader-type", "main")
}

