plugins {
    id("java")
    `maven-publish`
}

group = "me.heroostech.stomgui"
version = "v1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(libs.cityengine)
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