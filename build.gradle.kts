plugins {
    id("java")
    `maven-publish`
}

group = "me.heroostech.stomgui"
version = "v1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.minestom)
    implementation(libs.citystom)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

publishing {
    publications {
        create<MavenPublication>("minestom") {
            groupId = "me.heroostech.stomgui"
            artifactId = "StomGUI"
            version = "v1.0.0"

            from(components["java"])
        }
    }
}