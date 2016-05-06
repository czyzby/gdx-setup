package com.github.czyzby.setup

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.czyzby.autumn.context.ContextInitializer
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner
import com.github.czyzby.autumn.mvc.application.AutumnApplication
import com.github.czyzby.setup.config.Configuration
import com.github.czyzby.setup.views.Extension
import com.github.czyzby.setup.views.GdxPlatform
import com.github.czyzby.setup.views.JvmLanguage
import com.github.czyzby.setup.views.ProjectTemplate
import org.lwjgl.opengl.Display

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.title = "gdx-setup"
    config.width = Configuration.WIDTH
    config.height = Configuration.HEIGHT
    LwjglApplicationConfiguration.disableAudio = true
    // TODO config.decorated = false // (LibGDX 1.9.3)
    System.setProperty("org.lwjgl.opengl.Window.undecorated", "true")
    arrayOf(256, 128, 64, 32, 16).forEach { config.addIcon("icons/libgdx$it.png", Files.FileType.Classpath) }
    LwjglApplication(object : AutumnApplication(DesktopClassScanner(), Root::class.java) {
        override fun registerDefaultComponentAnnotations(initializer: ContextInitializer) {
            super.registerDefaultComponentAnnotations(initializer)
            initializer.scanFor(Extension::class.java, ProjectTemplate::class.java, JvmLanguage::class.java,
                    GdxPlatform::class.java)
        }
    }, config)
}

/**
 * Application's scanning root.
 * @author MJ
 */
class Root
