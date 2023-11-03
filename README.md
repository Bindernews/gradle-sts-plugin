# Slay the Spire Gradle Plugin

[GitHub](https://github.com/bindernews/gradle-sts-plugin)

This plugin provides various utilities for creating Slay the Spire mods.
In particular, it helps with locating dependencies such as `ModTheSpire.jar`
and `BaseMod.jar`, as well as providing a convenient way to run your
mod by using `MtsExec`. 

## Status
This plugin is still a work-in-progress, please report any issues on GitHub.

## Usage
Add the following lines to `settings.gradle.kts` and `build.gradle.kts` respectively.

```
// setting.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://maven.bindernews.net/")
        }
    }
}

// build.gradle.kts
plugins {
    // ...
    id("net.bindernews.gradle.stsplugin") version "0.0.2"
}

modMeta {
    // Reads the gradle properties `steam.dir` and optionally `sts.home`
    // to locate your steam workshop directory and slay the spire install directory.
    searchFromProperties()
    // When searching for mods, search these two directories before searching the workshop.
    searchPaths.addAll(files("$rootDir/lib", "$stsHome/mods"))
    
    // Add ModTheSpire, BaseMod, and StSLib
    addDefaultMods()
    // Add any custom mod dependencies here, with their workshop ID
    create("downfall.jar") { workshopId = "1610056683" }
    create("TS05_Marisa.jar") { workshopId = "1614104912" }
    create("Bestiary.jar") { workshopId = "2285965269" }
}

dependencies {
    // other dependencies...
    // Base game
    implementation(modMeta.baseGameJar)
    // Mods, both required and optional dependencies
    implementation(files(modMeta.findMods(
        "ModTheSpire.jar",
        "BaseMod.jar"
    )))
}

// This task will let you run your mod easily from your IDE, or from gradle.
// In IntelliJ you can create a Gradle run configuration for this task
// and remove "Debug Gradle scripts" from that configuration, making it work
// as if you were debugging ModTheSpire directly.
tasks.register<MtsExec>("runMts") {
    // The installJar task is automatically created, and copies
    // the generated jar file into Slay the Spire's /mods directory.
    dependsOn("installJar")
    mods = listOf("basemod", "stslib", modName)
    args("--skip-intro")
}
```


