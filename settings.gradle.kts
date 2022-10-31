@file:Suppress("UnstableApiUsage")

rootProject.name = "ExampleExtension"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("cityengine", "me.heroostech.cityengine:CityEngine:v1.0.0")
            library("lombok", "org.projectlombok:lombok:1.18.24")
        }
    }
}