package com.github.czyzby.setup.services

import com.badlogic.gdx.Gdx
import com.github.czyzby.autumn.annotation.Component
import com.github.czyzby.autumn.annotation.Initiate
import com.github.czyzby.setup.util.GLFWIconSetter

/**
 * Invokes [GLFWIconSetter] to change application icon after startup.
 * @author Kotcrab
 */
@Component
class IconService {
    @Initiate
    fun setApplicationIcon() {
        try {
            GLFWIconSetter.newInstance().setIcon(Gdx.files.internal("icons/libgdx128.ico"),
                    Gdx.files.internal("icons/libgdx128.png"));
        } catch(throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}
