package com.github.czyzby.setup.data.templates.unofficial

import com.github.czyzby.setup.data.files.SourceFile
import com.github.czyzby.setup.data.libs.unofficial.AutumnMVC
import com.github.czyzby.setup.data.platforms.Assets
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.data.templates.Template
import com.github.czyzby.setup.views.ProjectTemplate

/**
 * A project template showing basic Autumn MVC usage.
 * @author MJ
 */
@ProjectTemplate
open class AutumnMvcBasicTemplate : Template {
    override val id: String = "autumnMvcBasicTemplate"
    protected open val generateSkin = true
    override val description: String
        get() = "Project template included launchers with [Autumn](https://github.com/czyzby/gdx-lml/tree/master/autumn) class scanners and a single [Autumn MVC](https://github.com/czyzby/gdx-lml/tree/master/mvc) view."

    override fun apply(project: Project) {
        super.apply(project)
        if (generateSkin) project.advanced.forceSkinGeneration()

        // Registering main class in GWT reflection pool:
        getReflectedClasses(project).forEach { project.reflected.add(it) }

        // Adding Autumn MVC dependency:
        AutumnMVC().initiate(project)

        // Adding example LML template file:
        addViews(project)
    }

    protected open fun getReflectedClasses(project: Project): Array<String> =
            arrayOf("${project.basic.rootPackage}.${project.basic.mainClass}")


    protected open fun addViews(project: Project) {
        project.files.add(SourceFile(projectName = Assets.ID, sourceFolderPath = "ui", packageName = "templates",
                fileName = "first.lml", content = """<!-- Note: you can get content assist thanks to DTD schema files. See the official LML page. -->
<window title="Example" style="border" defaultPad="4" oneColumn="true">
    This is a simple Autumn MVC view constructed with LML.
    <textButton onClick="setClicked" tablePad="8">Click me!</textButton>
</window>"""))
    }

    override fun getApplicationListenerContent(project: Project): String = """package ${project.basic.rootPackage};

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.autumn.mvc.stereotype.preference.Skin;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

/** The first application's view. See first.lml file for widgets layout. */
@View(id = "first", value = "ui/templates/first.lml", first = true)
public class ${project.basic.mainClass} implements ActionContainer {
    /** Default application size. */
    public static final int WIDTH = 640, HEIGHT = 480;
    /** Path to the skin files. */
    @Skin private final String skinFile = "ui/skin";

    /** Since this method is annotated with LmlAction and this class implements ActionContainer, this method will be
     * available in the LML template: first.lml
     * @param button its text will be changed. */
    @LmlAction("setClicked")
    public void changeButtonText(TextButton button) {
        button.setText("Clicked.");
    }
}"""

    override fun getDesktopLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new AutumnApplication(new DesktopClassScanner(), ${project.basic.mainClass}.class),
                getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "${project.basic.name}";
        configuration.width = ${project.basic.mainClass}.WIDTH;
        configuration.height = ${project.basic.mainClass}.HEIGHT;
        return configuration;
    }
}"""

    override fun getGwtLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.autumn.gwt.scanner.GwtClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(${project.basic.mainClass}.WIDTH, ${project.basic.mainClass}.HEIGHT);
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new AutumnApplication(new GwtClassScanner(), ${project.basic.mainClass}.class);
    }
}"""

    override fun getAndroidLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.czyzby.autumn.android.scanner.AndroidClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        initialize(new AutumnApplication(new AndroidClassScanner(), ${project.basic.mainClass}.class), configuration);
    }
}"""

    override fun getHeadlessLauncherContent(project: Project): String = """package ${project.basic.rootPackage}.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import ${project.basic.rootPackage}.${project.basic.mainClass};

/** Launches the headless application. Can be converted into a utilities project or a server application. */
public class HeadlessLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static Application createApplication() {
        // Note: you can use a custom ApplicationListener implementation for the headless project instead of ${project.basic.mainClass}.
        return new HeadlessApplication(new AutumnApplication(new DesktopClassScanner(), ${project.basic.mainClass}.class),
                getDefaultConfiguration());
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
        configuration.renderInterval = -1f; // When this value is negative, application is never rendered.
        return configuration;
    }
}"""
}
