package com.github.czyzby.setup.data.platforms

import com.github.czyzby.setup.data.gradle.GradleFile
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.views.GdxPlatform

/**
 * Represents headless application project.
 * @author MJ
 */
@GdxPlatform
class Headless : Platform {
    companion object {
        const val ID = "headless"
    }

    override val id = ID

    override fun createGradleFile(project: Project): GradleFile = HeadlessGradleFile(project)

    override fun initiate(project: Project) {
        // Headless project has no additional dependencies.
    }
}

/**
 * Represents the Gradle file of the headless project. Allows to set up a different Java version and launch the application
 * with "run" task.
 * @author MJ
 */
class HeadlessGradleFile(val project: Project) : GradleFile(Headless.ID) {
    init {
        dependencies.add("project(':${Core.ID}')")
        addDependency("com.badlogicgames.gdx:gdx-backend-headless:\$gdxVersion")
        addDependency("com.badlogicgames.gdx:gdx-platform:\$gdxVersion:natives-desktop")
    }

    override fun getContent(): String = """apply plugin: 'application'

sourceCompatibility = ${project.advanced.serverJavaVersion}
mainClassName = '${project.basic.rootPackage}.headless.HeadlessLauncher'
eclipse.project.name = appName + '-headless'

dependencies {
${joinDependencies(dependencies)}}
"""
}