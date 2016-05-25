package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.files.CopiedFile
import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform

/**
 * Represents Desktop backend.
 * @author MJ
 */
@GdxPlatform
class Desktop : Platform {
    companion object {
        const val ID = "desktop"
    }

    override val id = ID

    override fun createGradleFile(project: Project): GradleFile = DesktopGradleFile(project)

    override fun initiate(project: Project) {
        // Adding game icons:
        arrayOf(16, 32, 64, 128).forEach {
            val icon = "libgdx${it}.png"
            project.files.add(CopiedFile(projectName = ID, path = path("src", "main", "resources", icon),
                    original = path("icons", icon)))
        }

        addGradleTaskDescription(project, "run", "starts the application.")
        addGradleTaskDescription(project, "jar", "builds application's runnable jar, which can be found at `${id}/build/libs`.")
    }
}

/**
 * Gradle file of the desktop project.
 * @author MJ
 */
class DesktopGradleFile(val project: Project) : GradleFile(Desktop.ID) {
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("com.badlogicgames.gdx:gdx-backend-lwjgl:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-desktop")
    }

    override fun getContent(): String = """apply plugin: 'application'

sourceSets.main.resources.srcDirs += [ rootProject.file('assets').absolutePath ]
mainClassName = '${project.basic.rootPackage}.desktop.DesktopLauncher'
eclipse.project.name = appName + '-desktop'
sourceCompatibility = ${project.advanced.desktopJavaVersion}

dependencies {
${joinDependencies(dependencies)}}

jar {
  archiveName "${'$'}{appName}-${'$'}{version}.jar"
  from { configurations.compile.collect { zipTree(it) } }
  manifest {
    attributes 'Main-Class': project.mainClassName
  }
}

run {
  ignoreExitValue = true
}
"""

}
