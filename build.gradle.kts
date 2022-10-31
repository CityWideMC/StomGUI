plugins {
    id("java")
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