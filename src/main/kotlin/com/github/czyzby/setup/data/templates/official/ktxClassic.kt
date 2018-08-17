package com.github.czyzby.setup.data.templates.official

import com.github.czyzby.setup.data.files.CopiedFile
import com.github.czyzby.setup.data.files.path
import com.github.czyzby.setup.data.platforms.Assets
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.data.templates.KtxTemplate
import com.github.czyzby.setup.views.ProjectTemplate

/**
 * Draws Ktx logo at the center of the screen.
 * @author MJ
 * @author Original gdx-setup maintainers
 */
@ProjectTemplate(official = true)
open class KtxClassicTemplate : KtxTemplate {
    override val id = "ktxClassic"
    override val description: String
        get() = "Project template includes simple launchers and an KtxGame extension that draws Ktx logo."

    override fun apply(project: Project) {
        super.apply(project)
        project.files.add(CopiedFile(projectName = Assets.ID, original = path("generator", "templates", "ktxClassic",
                "ktx-logo.png"), path = "ktx-logo.png"))
    }

    override fun getApplicationListenerContent(project: Project): String = """package ${project.basic.rootPackage};

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use

class Screen : KtxScreen {
    private val image = Texture("ktx-logo.png")
    private val batch = SpriteBatch()

    override fun render(delta: Float) {
        clearScreen(0.8f, 0.8f, 0.8f)
        batch.use {
            it.draw(image, 47.5f, 140f)
        }
    }

    override fun dispose() {
        image.dispose()
        batch.dispose()
    }
}

class ${project.basic.mainClass} : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(Screen())
        setScreen<Screen>()
    }
}
"""
}
