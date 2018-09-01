package com.github.czyzby.setup.data.templates

import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.platforms.*
import com.github.czyzby.setup.data.project.Project

interface KtxTemplate : Template {

    override fun addApplicationListener(project: Project) {
        addSourceFile(project = project, platform = Core.ID, packageName = project.basic.rootPackage,
                fileName = "${project.basic.mainClass}.kt", content = getApplicationListenerContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun addDesktopLauncher(project: Project) {
        addSourceFile(project = project, platform = Desktop.ID, packageName = "${project.basic.rootPackage}.desktop",
                fileName = "DesktopLauncher.kt", content = getDesktopLauncherContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun getDesktopLauncherContent(project: Project): String = """@file:JvmName("DesktopLauncher")

package ${project.basic.rootPackage}.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

import ${project.basic.rootPackage}.${project.basic.mainClass}

/** Launches the desktop (LWJGL) application. */
fun main(args: Array<String>) {
    LwjglApplication(${project.basic.mainClass}(), LwjglApplicationConfiguration().apply {
        title = "${project.basic.name}"
        width = $width
        height = $height
        resizable = false
        intArrayOf(128, 64, 32, 16).forEach{
            addIcon("libgdx${"$"}it.png", Files.FileType.Internal)
        }
    })
}
"""

    override fun addAndroidLauncher(project: Project) {
        addSourceFile(project = project, platform = Android.ID, packageName = "${project.basic.rootPackage}.android",
                fileName = "AndroidLauncher.kt", content = getAndroidLauncherContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun getAndroidLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.android

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import ${project.basic.rootPackage}.${project.basic.mainClass}

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(${project.basic.mainClass}(), AndroidApplicationConfiguration())
    }
}
"""

    override fun addHeadlessLauncher(project: Project) {
        addSourceFile(project = project, platform = Headless.ID, packageName = "${project.basic.rootPackage}.headless",
                fileName = "HeadlessLauncher.kt", content = getHeadlessLauncherContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun getHeadlessLauncherContent(project: Project): String = """@file:JvmName("HeadlessLauncher")

package ${project.basic.rootPackage}.headless

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import ${project.basic.rootPackage}.${project.basic.mainClass}

/** Launches the headless application. Can be converted into a utilities project or a server application. */
fun main(args: Array<String>) {
    HeadlessApplication(${project.basic.mainClass}(), HeadlessApplicationConfiguration().apply {
        renderInterval = -1f
    })
}
"""

    override fun addLwjgl3Launcher(project: Project) {
        addSourceFile(project = project, platform = LWJGL3.ID, packageName = "${project.basic.rootPackage}.lwjgl3",
                fileName = "Lwjgl3Launcher.kt", content = getLwjgl3LauncherContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun getLwjgl3LauncherContent(project: Project): String = """@file:JvmName("Lwjgl3Launcher")

package ${project.basic.rootPackage}.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ${project.basic.rootPackage}.${project.basic.mainClass}

/** Launches the desktop (LWJGL3) application. */
fun main(args: Array<String>) {
    Lwjgl3Application(${project.basic.mainClass}(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("${project.basic.name}")
        setWindowedMode($width, $height)
        setResizable(false)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx${"$"}it.png" }.toTypedArray()))
    })
}
"""
}

