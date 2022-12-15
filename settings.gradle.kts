@file:Suppress("UnstableApiUsage")

rootProject.name = "StomGUI"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("minestom", "com.github.Minestom:Minestom:-SNAPSHOT")
            library("citystom", "com.github.CityWideMC:CityStom:e29aef03")
            library("lombok", "org.projectlombok:lombok:1.18.24")
            library("geyserutils", "me.heroostech.geyserutils:GeyserUtils:2.0")
        }
    }
}