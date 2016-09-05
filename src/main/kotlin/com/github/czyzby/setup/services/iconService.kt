package com.github.czyzby.setup.services

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.github.czyzby.autumn.annotation.Component
import com.github.czyzby.autumn.annotation.Initiate
import com.kotcrab.vis.ui.util.OsUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage

/**
 * Changes application icon after startup.
 * @author Kotcrab
 */
@Component
class IconService {
    @Initiate
    fun setApplicationIcon() {
        try {
            if (!OsUtils.isMac()) {
                val iconPaths = arrayOf("icons/libgdx16.png", "icons/libgdx32.png", "icons/libgdx64.png",
                        "icons/libgdx128.png")
                val pixmaps = iconPaths.map { Pixmap(Gdx.files.internal(it)) }

                val buffer = GLFWImage.malloc(iconPaths.size)
                pixmaps.forEach { pixmap ->
                    if (pixmap.format != Pixmap.Format.RGBA8888) {
                        throw IllegalStateException("Incorrect icon pixmap format, must be RGBA8888.")
                    }
                    val icon = GLFWImage.malloc().set(pixmap.width, pixmap.height, pixmap.pixels)
                    buffer.put(icon)
                    icon.free()
                }

                buffer.position(0)
                GLFW.glfwSetWindowIcon(GLFW.glfwGetCurrentContext(), buffer)
                buffer.free()

                pixmaps.forEach(Pixmap::dispose)
            }
        } catch(throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}
