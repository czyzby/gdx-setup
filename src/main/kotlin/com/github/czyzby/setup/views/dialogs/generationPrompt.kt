package com.github.czyzby.setup.views.dialogs

import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Timer
import com.github.czyzby.autumn.annotation.Inject
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogShower
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog
import com.github.czyzby.setup.views.MainView
import com.kotcrab.vis.ui.widget.VisDialog

@ViewDialog(id = "generation", value = "templates/dialogs/generation.lml", cacheInstance = true)
class GenerationPrompt : ViewDialogShower {
    @Inject private lateinit var interfaceService: InterfaceService;
    @Inject private lateinit var mainView: MainView

    override fun doBeforeShow(dialog: Window) {
        val popup = dialog as VisDialog;
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                mainView.createProject().generate()
                mainView.revalidateForm()
                popup.hide()
            }
        }, 0.41f); // 0.4 is the default fading time of dialogs.
    }
}