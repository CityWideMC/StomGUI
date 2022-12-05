plugins {
    id("java")
    `maven-publish`
}

group = "me.heroostech.stomgui"
version = "2.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.minestom)
    compileOnly(libs.citystom)
    compileOnly(libs.lombok)
    compileOnly(libs.geyserutils)
    annotationProcessor(libs.lombok)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.heroostech.stomgui"
            artifactId = "StomGUI"
            version = "2.0"

            from(components["java"])
        }
    }
}