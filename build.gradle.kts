import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

group = "me.steelph0enix"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/org.simpleframework/simple-xml
    implementation("org.simpleframework:simple-xml:2.7.1")
    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
}

tasks.test {
    useTestNG()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.steelph0enix.integracja.lab2.MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

application {
    mainClass.set("Main")
}