package com.github.czyzby.setup.views.dialogs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.Timer
import com.github.czyzby.autumn.annotation.Inject
import com.github.czyzby.autumn.mvc.component.i18n.LocaleService
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogShower
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog
import com.github.czyzby.kiwi.util.gdx.collection.pooled.PooledList
import com.github.czyzby.lml.annotation.LmlActor
import com.github.czyzby.setup.views.MainView
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisTextArea
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Displayed after generation request was sent.
 * @author MJ
 */
@ViewDialog(id = "generation", value = "templates/dialogs/generation.lml", cacheInstance = false)
class GenerationPrompt : ViewDialogShower {
    @Inject private lateinit var interfaceService: InterfaceService;
    @Inject private lateinit var locale: LocaleService;
    @Inject private lateinit var mainView: MainView

    @LmlActor("close", "exit") private lateinit var buttons: ObjectSet<Button>
    @LmlActor("console") private lateinit var console: VisTextArea
    @LmlActor("scroll") private lateinit var scrollPane: ScrollPane
    // TODO VisTextArea should be read-only (not disabled) and ScrollPane-compatible.

    private val executor = Executors.newSingleThreadExecutor()
    private val loggingBuffer = ConcurrentLinkedQueue<String>()

    override fun doBeforeShow(dialog: Window) {
        executor.execute {
            logNls("copyStart")
            mainView.createProject().generate()
            logNls("copyEnd")
            mainView.revalidateForm()
            // TODO check if Gradle wrapper was included, run gradle tasks
            logNls("generationEnd")
            buttons.forEach { it.isDisabled = false }
        }
    }

    fun logNls(bundleLine: String) = log(locale.i18nBundle.get(bundleLine))
    fun log(message: String) {
        loggingBuffer.offer(message)
        Gdx.app.postRunnable {
            while (loggingBuffer.isNotEmpty()) {
                console.text += loggingBuffer.poll() + "\n"
            }
            scrollPane.invalidateHierarchy()
            scrollPane.addAction(Actions.run { scrollPane.scrollPercentY = 1f })
        }
    }
}
