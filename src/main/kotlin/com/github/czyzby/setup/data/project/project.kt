package com.github.czyzby.setup.data.project

import com.github.czyzby.setup.data.files.*
import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.gradle.RootGradleFile
import com.github.czyzby.setup.data.langs.Java
import com.github.czyzby.setup.data.platforms.Assets
import com.github.czyzby.setup.data.platforms.Platform
import com.github.czyzby.setup.data.templates.Template
import com.github.czyzby.setup.views.AdvancedData
import com.github.czyzby.setup.views.BasicProjectData
import com.github.czyzby.setup.views.ExtensionsData
import com.github.czyzby.setup.views.LanguagesData
import java.io.File

class Project(val basic: BasicProjectData, val platforms: Map<String, Platform>, val advanced: AdvancedData,
              val languages: LanguagesData, val extensions: ExtensionsData, val template: Template) {
    private val gradleFiles: Map<String, GradleFile>
    val files = mutableListOf<ProjectFile>()
    val rootGradle: RootGradleFile
    val properties = mutableMapOf(
            "org.gradle.daemon" to "true",
            "org.gradle.jvmargs" to "-Xms128m -Xmx512m",
            "org.gradle.configureondemand" to "true")
    val postGenerationTasks = mutableListOf<(Project) -> Unit>()
    val gwtInherits = mutableSetOf<String>()
    val reflected = mutableSetOf<String>()

    init {
        gradleFiles = mutableMapOf<String, GradleFile>()
        rootGradle = RootGradleFile(this)
        platforms.forEach { gradleFiles[it.key] = it.value.createGradleFile(this) }
    }

    fun hasPlatform(id: String): Boolean = platforms.containsKey(id)

    @Suppress("UNCHECKED_CAST")
    fun <P : Platform> getPlatform(id: String): P = platforms.get(id)!! as P

    fun getGradleFile(id: String): GradleFile = gradleFiles.get(id)!!

    fun generate() {
        addBasicFiles()
        addJvmLanguagesSupport()
        addExtensions()
        addPlatforms()
        template.apply(this)
        addSkinAssets()
        saveProperties()
        saveFiles()
        // Invoking post-generation tasks:
        postGenerationTasks.forEach { it(this) }
    }

    private fun addBasicFiles() {
        // Adding global assets folder:
        files.add(SourceDirectory(Assets.ID, ""))
        // Adding .gitignore:
        files.add(CopiedFile(path = ".gitignore", original = path("generator", "gitignore")))
    }

    private fun addJvmLanguagesSupport() {
        Java().initiate(this) // Java is supported by default.
        languages.getSelectedLanguages().forEach {
            it.initiate(this)
            properties[it.id + "Version"] = languages.getVersion(it.id)
        }
        languages.appendSelectedLanguagesVersions(this)
    }

    private fun addExtensions() {
        extensions.getSelectedOfficialExtensions().forEach { it.initiate(this) }
        extensions.getSelectedThirdPartyExtensions().forEach { it.initiate(this) }
    }

    private fun addPlatforms() {
        platforms.values.forEach { it.initiate(this) }
        SettingsFile(platforms.values).save(basic.destination)
    }

    private fun saveFiles() {
        rootGradle.save(basic.destination)
        gradleFiles.values.forEach { it.save(basic.destination) }
        files.forEach { it.save(basic.destination) }
    }

    private fun saveProperties() {
        // Adding LibGDX version property:
        properties["gdxVersion"] = advanced.gdxVersion
        PropertiesFile(properties).save(basic.destination)
    }

    private fun addSkinAssets() {
        if (advanced.generateSkin) {
            // Adding raw assets directory:
            files.add(SourceDirectory("raw", "ui"))
            // Adding GUI assets directory:
            files.add(SourceDirectory(Assets.ID, "ui"))
            // Adding JSON file:
            files.add(CopiedFile(projectName = Assets.ID, path = path("ui", "skin.json"),
                    original = path("generator", "assets", "ui", "skin.json")))

            // Copying raw assets - internal files listing doesn't work, so we're hard-coding raw/ui content:
            arrayOf("check.png", "check-on.png", "dot.png", "knob-h.png", "knob-v.png", "line-h.png", "line-v.png",
                    "pack.json", "rect.png", "select.9.png", "square.png", "tree-minus.png", "tree-plus.png",
                    "window-border.9.png", "window-resize.9.png").forEach {
                files.add(CopiedFile(projectName = "raw", path = "ui${File.separator}$it",
                        original = path("generator", "raw", "ui", it)))
            }

            // Appending "pack" task to root Gradle:
            postGenerationTasks.add({
                basic.destination.child(rootGradle.path).writeString("""
// Run `gradle pack` task to generate skin.atlas file at assets/ui.
import com.badlogic.gdx.tools.texturepacker.TexturePacker
task pack << {
  // Note that if you need multiple atlases, you can duplicate the
  // TexturePacker.process invocation and change paths to generate
  // additional atlases with this task.
  TexturePacker.process(
    'raw/ui',           // Raw assets path.
    'assets/ui',        // Output directory.
    'skin'              // Name of the generated atlas (without extension).
  )
}""", true, "UTF-8");
            })
        }
    }
}