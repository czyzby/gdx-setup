package com.github.czyzby.setup.views

import com.badlogic.gdx.Version
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.czyzby.autumn.annotation.Inject
import com.github.czyzby.autumn.mvc.stereotype.View
import com.github.czyzby.lml.annotation.LmlAction
import com.github.czyzby.lml.annotation.LmlActor
import com.github.czyzby.lml.annotation.LmlAfter
import com.github.czyzby.lml.annotation.LmlInject
import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.parser.action.ActionContainer
import com.github.czyzby.lml.vis.parser.impl.tag.TabbedPaneLmlTag
import com.github.czyzby.lml.vis.ui.VisFormTable
import com.github.czyzby.setup.data.platforms.Android
import com.github.czyzby.setup.data.project.Project
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane

/**
 * Main application's view. Displays application's menu.
 * @author MJ
 */
@View(id = "main", value = "templates/main.lml", first = true)
class MainView : ActionContainer {
    @LmlInject private lateinit var basicData: BasicProjectData
    @LmlInject private lateinit var advancedData: AdvancedData
    @LmlInject @Inject private lateinit var platformsData: PlatformsData
    @LmlInject @Inject private lateinit var languagesData: LanguagesData
    @LmlInject @Inject private lateinit var extensionsData: ExtensionsData
    @LmlInject @Inject private lateinit var templatesData: TemplatesData
    @LmlActor("form") private lateinit var form: VisFormTable

    @LmlAction("chooseDirectory")
    fun chooseDirectory(file: FileHandle?) {
        if (file != null) {
            basicData.setDestination(file.path())
        }
    }

    @LmlAction("chooseSdkDirectory")
    fun chooseSdkDirectory(file: FileHandle?) {
        if (file != null) {
            basicData.setAndroidSdkPath(file.path())
        }
    }

    @LmlAction("toggleAndroid")
    fun toggleAndroidPlatform(button: Button) {
        if (button.name == Android.ID) {
            platformsData.toggleAndroidPlatform(button.isChecked)
            revalidateForm()
        }
    }

    @LmlAfter fun initiateVersions(parser: LmlParser) {
        languagesData.assignVersions(parser)
        extensionsData.assignVersions(parser)
    }

    fun revalidateForm() {
        form.formValidator.validate()
    }

    @LmlAction("platforms") fun getPlatforms(): Iterable<*> = platformsData.platforms.keys.sorted()
    @LmlAction("show") fun getTabShowingAction(): Action = Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.1f))
    @LmlAction("hide") fun getTabHidingAction(): Action = Actions.fadeOut(0.1f)
    @LmlAction("gdxVersion") fun getGdxVersion(): String = Version.VERSION
    @LmlAction("gwtVersions") fun getGwtVersions(): Array<String> = arrayOf("2.6.0", "2.6.1", "2.7.0", "2.8.0-beta1")
    @LmlAction("jvmLanguages") fun getLanguages(): Array<String> = languagesData.languages
    @LmlAction("jvmLanguagesVersions") fun getLanguagesVersions(): Array<String> = languagesData.versions
    @LmlAction("templates") fun getTemplates(): Array<String> = templatesData.templates.map { it.id }.sorted().toTypedArray()

    @LmlAction("officialExtensions") fun getOfficialExtensions(): Array<String> =
            extensionsData.official.map { it.id }.sorted().toTypedArray()

    @LmlAction("officialExtensionsUrls") fun getOfficialExtensionsUrls(): Array<String> =
            extensionsData.official.sortedBy { it.id }.map { it.url }.toTypedArray()

    @LmlAction("thirdPartyExtensions") fun getThirdPartyExtensions(): Array<String> =
            extensionsData.thirdParty.map { it.id }.sorted().toTypedArray()

    @LmlAction("thirdPartyExtensionsVersions") fun getThirdPartyExtensionsVersions(): Array<String> =
            extensionsData.thirdParty.sortedBy { it.id }.map { it.defaultVersion }.toTypedArray()

    @LmlAction("thirdPartyExtensionsUrls") fun getThirdPartyExtensionsUrls(): Array<String> =
            extensionsData.thirdParty.sortedBy { it.id }.map { it.url }.toTypedArray()

    @LmlAction("initTabs") fun initiateTabbedPane(tabbedPane: TabbedPane.TabbedPaneTable) {
        tabbedPane.tabbedPane.tabsPane.horizontalFlowGroup.spacing = 2f
    }

    fun createProject(): Project = Project(basicData, platformsData.getSelectedPlatforms(),
            advancedData, languagesData, extensionsData, templatesData.getSelectedTemplate())

}


