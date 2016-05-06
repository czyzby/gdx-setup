package com.github.czyzby.setup.actions

import com.badlogic.gdx.Gdx
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer
import com.github.czyzby.kiwi.util.common.Strings
import com.github.czyzby.lml.annotation.LmlAction
import com.github.czyzby.lml.parser.action.ActionContainer
import com.kotcrab.vis.ui.widget.VisTextField


/**
 * Contains actions available for all dialogs and views.
 * @author MJ
 */
@ViewActionContainer("global")
class GlobalActionContainer : ActionContainer {
    @LmlAction("showSite") fun showLibGdxWebsite() = Gdx.net.openURI("http://libgdx.badlogicgames.com/")

    @LmlAction("fileNameFilter")
    fun isValidFileNameCharacter(character: Char): Boolean = Character.isDigit(character) ||
            Character.isLetter(character) || character == '-' || character == '_'

    @LmlAction("isValidFile")
    fun isValidFileName(input: String): Boolean = Strings.isNotBlank(input) && input.matches(Regex("[-\\w]+"))

    @LmlAction("javaClassFilter")
    fun isValidJavaCharacter(character: Char): Boolean = Character.isJavaIdentifierPart(character)

    @LmlAction("javaPackageFilter")
    fun isValidJavaPackageCharacter(character: Char): Boolean = Character.isJavaIdentifierPart(character) || character == '.'

    @LmlAction("isValidClass")
    fun isValidClassName(input: String): Boolean {
        if (Strings.isBlank(input) || !Character.isJavaIdentifierStart(input[0])) {
            return false
        } else if (input.length == 1) {
            return true
        }
        for (id in 1..input.length - 1) {
            if (!Character.isJavaIdentifierPart(input[id])) {
                return false
            }
        }
        return true
    }

    @LmlAction("isValidPackage")
    fun isValidPackageName(input: String): Boolean {
        if (Strings.isBlank(input) || !Character.isJavaIdentifierStart(input[0]) || input.contains("..") || input.endsWith('.')) {
            return false
        } else if (input.length == 1) {
            return true
        }
        for (id in 1..input.length - 1) {
            if (!Character.isJavaIdentifierPart(input[id]) && input[id] != '.') {
                return false
            }
        }
        return true
    }

    @LmlAction("close")
    fun noOp() {
        // Empty dialog closing utility.
    }
}
