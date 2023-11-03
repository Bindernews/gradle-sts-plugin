package net.bindernews.gradle.stsplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class StSPlugin : Plugin<Project> {

    private var modMetaExt: ModMetaExtension? = null

    override fun apply(project: Project) {
        modMetaExt = ModMetaExtension(project)
        project.extensions.add("modMeta", modMetaExt!!)

        // Code MUST target Java 8
        project.tasks.named("compileJava", JavaCompile::class.java) {
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
            options.encoding = "UTF-8"
        }

        val jarTask = project.tasks.named("jar", Jar::class.java)
        project.tasks.named("installJar", Copy::class.java) {
            group = "build"
            description = "Copy the mod jar file into the StS mods directory"
            mustRunAfter(jarTask)
            from(jarTask.map { it.archiveFile })
            into(modMetaExt!!.stsHome.dir("mods"))
        }

        // Do some extra things if using kotlin
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            KotlinHandler.apply(project)
        }
    }

    fun getModMeta(): ModMetaExtension = modMetaExt!!

    // Separate class so class-loader isn't triggered unless kotlin plugin is found
    object KotlinHandler {
        fun apply(project: Project) {
            project.tasks.named("compileKotlin", KotlinJvmCompile::class.java) {
                kotlinOptions.jvmTarget = "1.8"
                kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
            }
        }
    }
}


