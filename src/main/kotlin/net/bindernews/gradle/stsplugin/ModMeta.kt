package net.bindernews.gradle.stsplugin

import org.gradle.api.Named
import java.io.File
import java.io.Serializable

class ModMeta(private val name: String) : Named, Serializable {
    /**
     * List of file names for the mod, generally ending in `.jar`.
     */
    val fileNames: MutableList<String> = arrayListOf()
    /**
     * The mod's workshop ID, or `""` if it's not a workshop mod.
     */
    var workshopId: String = ""
        set(value) {
            field = value
            dirty = true
        }

    @Transient
    internal var dirty: Boolean = false

    init {
        if (name.endsWith(".jar")) {
            fileNames.add(name)
        }
    }

    override fun getName(): String = name

    /**
     * Search in the given directory for one of the files in [fileNames].
     */
    fun searchIn(dir: File): File? {
        for (n in fileNames) {
            val child = dir.resolve(n)
            if (child.isFile) {
                return child
            }
        }
        return null
    }
}