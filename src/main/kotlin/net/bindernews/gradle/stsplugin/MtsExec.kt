package net.bindernews.gradle.stsplugin

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.JavaExec
import org.gradle.process.CommandLineArgumentProvider
import java.lang.String.join

/**
 * An extension to [JavaExec] which is pre-configured to run ModTheSpire.
 */
abstract class MtsExec : JavaExec() {
    @get:Input
    abstract val mods: ListProperty<String>

    init {
        val modMeta = project.extensions.getByName("modMeta") as ModMetaExtension
        classpath = project.files(modMeta.findMod("ModTheSpire.jar"))
        workingDir = modMeta.stsHome.asFile.get()
        mainClass.convention("com.evacipated.cardcrawl.modthespire.Loader")
        javaLauncher.convention(SlayTheSpireJvm(modMeta))
        argumentProviders.add(CommandLineArgumentProvider {
            if (mods.get().size > 0) {
                listOf("--mods", join(",", mods.get()))
            } else {
                listOf()
            }
        })
    }
}