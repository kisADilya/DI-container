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
    // https://mvnrepository.com/artifact/net.javacrumbs.json-unit/json-unit-assertj
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.0")

    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}