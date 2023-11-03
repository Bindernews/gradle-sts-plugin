package net.bindernews.gradle.stsplugin

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.internal.os.OperatingSystem
import org.gradle.jvm.toolchain.JavaInstallationMetadata
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaLauncher

class SlayTheSpireJvm(private val modMeta: ModMetaExtension) : JavaLauncher {
    private val jreDir: Directory by lazy { modMeta.stsHome.get().dir("jre") }

    override fun getMetadata(): JavaInstallationMetadata {
        return SimpleJavaInstallationMetadata(
            JavaLanguageVersion.of(8),
            "1.8.0_144-b01",
            "25.144-b01",
            "Oracle Corporation",
            jreDir,
            false,
        )
    }

    override fun getExecutablePath(): RegularFile {
        val javaName = OperatingSystem.current().getExecutableName("java")
        return jreDir.file("bin/$javaName")
    }

    data class SimpleJavaInstallationMetadata(
        private val languageVersion: JavaLanguageVersion,
        private val javaRuntimeVersion: String,
        private val jvmVersion: String,
        private val vendor: String,
        private val installationPath: Directory,
        @get:JvmName("_isCurrentJvm")
        val isCurrentJvm: Boolean,
    ) : JavaInstallationMetadata {
        override fun getLanguageVersion() = languageVersion
        override fun getJavaRuntimeVersion(): String = javaRuntimeVersion
        override fun getJvmVersion(): String = jvmVersion
        override fun getVendor(): String = vendor
        override fun getInstallationPath(): Directory = installationPath
        override fun isCurrentJvm(): Boolean = isCurrentJvm
    }
}