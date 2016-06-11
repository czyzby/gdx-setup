package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform

/**
 * Represents the JogAmp backend.
 * @author Julien Gouesse
 */
@GdxPlatform
class JogAmp : Platform {
    companion object {
        const val ID = "jogamp"
    }

    override val id = ID
    override fun createGradleFile(project: Project): GradleFile = JogAmpGradleFile(project)
    override fun initiate(project: Project) {
        // JogAmp requires no additional dependencies.

        addGradleTaskDescription(project, "run", "starts the application.")
        addGradleTaskDescription(project, "jar", "builds application's runnable jar, which can be found at `${id}/build/libs`.")
    }
}
/**
 * Gradle file of the JogAmp project.
 * @author MJ
 */
class JogAmpGradleFile(val project: Project) : GradleFile(JogAmp.ID) {
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("org.jogamp.libgdx:gdx-backend-jogamp:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-desktop")
    }

    override fun getContent(): String = """apply plugin: 'application'

sourceSets.main.resources.srcDirs += [ rootProject.file('assets').absolutePath ]
mainClassName = '${project.basic.rootPackage}.jogamp.JogAmpLauncher'
eclipse.project.name = appName + '-jogamp'
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
"""

}
