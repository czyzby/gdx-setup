package com.github.czyzby.setup.data.platforms

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
        // Desktop platform requires no additional dependencies.

        addGradleTaskDescription(project, "run", "starts the application.")
        addGradleTaskDescription(project, "jar", "builds application's runnable jar, which can be found at `${id}/build/libs`.")
    }
}

/**
 * Gradle file of the desktop project. Should contain dependencies to one of desktop platform native libraries.
 * @author MJ
 */
class DesktopGradleFile(val project: Project) : GradleFile(Desktop.ID) {
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("com.badlogicgames.gdx:gdx-backend-lwjgl:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-desktop")
    }

    override fun getContent(): String = """apply plugin: 'application'

sourceSets.main.resources.srcDirs = [ rootProject.file('assets').absolutePath ]
mainClassName = '${project.basic.rootPackage}.desktop.DesktopLauncher'
eclipse.project.name = appName + '-desktop'

dependencies {
${joinDependencies(dependencies)}}

jar {
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
