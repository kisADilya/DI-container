plugins {
    id("java")
}

group = "ru.nsu.kisadilya.di-container"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
}

tasks.test {
    useJUnitPlatform()
}