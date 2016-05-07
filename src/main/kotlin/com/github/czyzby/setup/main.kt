package com.github.czyzby.setup

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.czyzby.autumn.context.ContextInitializer
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner
import com.github.czyzby.autumn.mvc.application.AutumnApplication
import com.github.czyzby.setup.config.Configuration
import com.github.czyzby.setup.views.Extension
import com.github.czyzby.setup.views.GdxPlatform
import com.github.czyzby.setup.views.JvmLanguage
import com.github.czyzby.setup.views.ProjectTemplate

fun main(args: Array<String>) {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("gdx-setup")
    config.setWindowedMode(Configuration.WIDTH, Configuration.HEIGHT)
    config.disableAudio(true)
    config.setDecorated(false)
    // arrayOf(256, 128, 64, 32, 16).forEach { config.addIcon("icons/libgdx$it.png", Files.FileType.Classpath) }
    Lwjgl3Application(object : AutumnApplication(DesktopClassScanner(), Root::class.java) {
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
