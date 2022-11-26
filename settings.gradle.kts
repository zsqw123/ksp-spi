pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "spi-ksp"
include("spi-api")
include("spi-plugin")
include("demo-main")
include("demo-child")
include("demo-api")
