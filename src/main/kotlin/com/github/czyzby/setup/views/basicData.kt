package com.github.czyzby.setup.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.github.czyzby.lml.annotation.LmlActor
import com.kotcrab.vis.ui.widget.VisTextField
import javax.print.attribute.standard.Destination

/**
 * Filled by the LML parser, this class contains references to basic project data widgets.
 * @author MJ
 */
class BasicProjectData {
    @LmlActor("name") private lateinit var nameField: VisTextField
    @LmlActor("package") private lateinit var rootPackageField: VisTextField
    @LmlActor("class") private lateinit var mainClassField: VisTextField
    @LmlActor("destination") private lateinit var destinationField: VisTextField
    @LmlActor("androidSdk") private lateinit var androidSdkPathField: VisTextField

    @LmlActor("mkdirs") private lateinit var mkdirsButton: Button
    @LmlActor("clearFolder") private lateinit var clearButton: Button

    val name: String
        get() = nameField.text
    val rootPackage: String
        get() = rootPackageField.text
    val mainClass: String
        get() = mainClassField.text
    val destination: FileHandle
        get() = Gdx.files.absolute(destinationField.text)
    val androidSdk: FileHandle
        get() = Gdx.files.absolute(androidSdkPathField.text)

    fun setDestination(path: String) {
        destinationField.text = path
    }

    fun setAndroidSdkPath(path: String) {
        androidSdkPathField.text = path
    }

    fun revalidateDirectoryUtilityButtons() {
        try {
            val folder = destination
            if (folder.exists()) {
                mkdirsButton.isDisabled = true
                if (folder.isDirectory && folder.list().size > 0) {
                    clearButton.isDisabled = false
                } else {
                    clearButton.isDisabled = true
                }
            } else {
                mkdirsButton.isDisabled = false
                clearButton.isDisabled = true
            }
        } catch(exception: Exception) {
            // Somewhat expected for invalid input.
            clearButton.isDisabled = true
            mkdirsButton.isDisabled = true
        }
    }
}
