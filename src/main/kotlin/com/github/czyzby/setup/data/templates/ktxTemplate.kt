package com.github.czyzby.setup.data.templates

import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.platforms.Core
import com.github.czyzby.setup.data.platforms.Desktop
import com.github.czyzby.setup.data.project.Project

interface KtxTemplate : Template {
    override val isKtxTemplate: Boolean
        get() = true

    override fun addApplicationListener(project: Project) {
        addSourceFile(project = project, platform = Core.ID, packageName = project.basic.rootPackage,
                fileName = "${project.basic.mainClass}.kt", content = getApplicationListenerContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun addDesktopLauncher(project: Project) {
        addSourceFile(project = project, platform = Desktop.ID, packageName = "${project.basic.rootPackage}.desktop",
                fileName = "DesktopLauncher.kt", content = getDesktopLauncherContent(project), sourceFolderPath = path("src", "main", "kotlin"))
    }

    override fun getDesktopLauncherContent(project: Project): String = """@file:JvmName("DesktopLauncher")

package ${project.basic.rootPackage}.desktop;

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

import ${project.basic.rootPackage}.${project.basic.mainClass};

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
}

