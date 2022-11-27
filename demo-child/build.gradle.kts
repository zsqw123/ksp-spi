plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "com.zsu"
version = "1.0-SNAPSHOT"

dependencies {
    ksp(project(":spi-plugin"))
    implementation(project(":spi-api"))
    implementation(project(":demo-api"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

ksp {
    arg("spi-loader-name", project.name)
}
