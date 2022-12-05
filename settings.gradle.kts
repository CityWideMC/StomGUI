@file:Suppress("UnstableApiUsage")

rootProject.name = "StomGUI"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("minestom", "com.github.Minestom:Minestom:-SNAPSHOT")
            library("citystom", "me.heroostech.citystom:CityStom:v1.0.0")
            library("lombok", "org.projectlombok:lombok:1.18.24")
            library("geyserutils", "me.heroostech.geyserutls:GeyserUtils:2.0")
        }
    }
}