import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.bindernews.gradle.stsplugin"
version = "0.0.2"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("maven-publish")
    id("java-gradle-plugin")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.9.20")
}

gradlePlugin {
    plugins {
        create(project.name) {
            id = "net.bindernews.gradle.stsplugin"
            implementationClass = "net.bindernews.gradle.stsplugin.StSPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/bindernews/sts-dev")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("TOKEN")
            }
        }
    }
//    publications {
//        register<MavenPublication>("gpr") {
//            from(components["java"])
//        }
//    }
}

tasks.getByName<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}

